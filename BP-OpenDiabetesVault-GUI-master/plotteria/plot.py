# Copyright (C) 2017 Jens Heuschkel, Philipp Thomasberger
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
from numpy.distutils.exec_command import temp_file_name

try:
    import csv
    import datetime
    import json
    import matplotlib.pyplot as plt
    import matplotlib.dates as dates
    import matplotlib.lines as lines
    import matplotlib.patches as patches
    import matplotlib.transforms as transforms
    from matplotlib.ticker import MultipleLocator
    import re
    import os.path
    import configparser
    import sys
    import shutil
    import pylab
    import numpy as np
    import random
    from optparse import OptionParser

except ImportError as err:
    print "[ModuleError]", err, "(needed libraries: matplotlib, numpy)"
    exit(0)
# const
# TODO check required fields only

csvDataFormat = ['date','time','bgValue','cgmValue','cgmRawValue','cgmAlertValue','glucoseAnnotation','basalValue','basalAnnotation','bolusValue','bolusAnnotation','bolusCalculationValue','mealValue','pumpAnnotation','exerciseTimeValue','exerciseAnnotation','heartRateValue','heartRateVariabilityValue','stressBalanceValue','stressValue','sleepValue','sleepAnnotation','locationAnnotation', 'mlCgmValue', 'pumpCgmPredictionValue', 'otherAnnotation']
slicesDataFormat = ['date','time','duration','type','filter']

genericsColors = {'cgmGenerics':{}, 'bolusCalculationGenerics':{}, 'bolusGenerics':{}, 'basalGenerics': {}, 'symbolGenerics': {}}

## helper functions
def dateParser(date, time):
    return datetime.datetime.strptime(date + time, '%d.%m.%y%H:%M')

def absPathOnly():
    return os.path.dirname(os.path.realpath(sys.argv[0]))
def absPath(filename):
    return os.path.join(absPathOnly(), filename)

def parseDataset(csvFileName):
    with open(csvFileName, 'rU') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', dialect=csv.excel_tab)
        try:
            header = reader.next()
        except StopIteration:
            print "[Error] Given dataset is empty"
            exit(0)

        for headerField in csvDataFormat:
            if headerField not in header:
                print '[FormatError] File \'' + csvFileName + '\' has a wrong header'
                exit(0)

        dataset = []

        for row in reader:
            dataset.append(dict(zip(header, row)))

    if not dataset:
        print "[Error] Given dataset is empty"
        exit(0)

    return dataset

def dataSubset(dataset, beginDate, duration):
    endDate = beginDate + datetime.timedelta(minutes=duration)

    locationCache = ""
    stressCache = ""
    sleepCache = ""
    basalCache = ""
    exerciseCache = ""

    datasubset = []

    cacheValuesExist = False

    for d in dataset:
        tempDate = dateParser(d['date'], d['time'])
        if tempDate < beginDate:
            if d['locationAnnotation']:
                locationCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                               'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': '', 'heartRateValue': '',
                               'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                               'basalAnnotation': '', 'locationAnnotation': '', 'stressValue': '',
                               'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                               'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                               'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': d['locationAnnotation'], 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                               'otherAnnotation': ''}
                cacheValuesExist = True
            if d['stressValue']:
                stressCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                               'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': '', 'heartRateValue': '',
                               'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                               'basalAnnotation': '', 'locationAnnotation': '', 'stressValue': d['stressValue'],
                               'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                               'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                               'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': '', 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                               'otherAnnotation': ''}
                cacheValuesExist = True
            if d['sleepAnnotation']:
                if dateParser(d['date'], d['time']) + datetime.timedelta(minutes=float(d['sleepValue'])) > beginDate:
                    sleepCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                                   'bolusValue': '', 'sleepValue': (float(d['sleepValue']) - ((beginDate - dateParser(d['date'], d['time'])).total_seconds() / 60)), 'exerciseAnnotation': '', 'heartRateValue': '',
                                   'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                                   'basalAnnotation': '', 'locationAnnotation': '', 'stressValue': '',
                                   'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                                   'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                                   'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': d['sleepAnnotation'], 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                                   'otherAnnotation': ''}
                cacheValuesExist = True
            if d['basalValue']:
                basalCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': d['basalValue'],
                               'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': '', 'heartRateValue': '',
                               'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                               'basalAnnotation': d['basalAnnotation'], 'locationAnnotation': '', 'stressValue': '',
                               'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                               'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                               'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': '', 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                               'otherAnnotation': ''}
                cacheValuesExist = True
            if d['exerciseAnnotation']:
                if d['exerciseTimeValue'] and dateParser(d['date'], d['time']) + datetime.timedelta(minutes=float(d['exerciseTimeValue'])) > beginDate:
                    exerciseCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                                   'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': d['exerciseAnnotation'], 'heartRateValue': '',
                                   'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '',
                                   'bolusCalculationValue': '', 'basalAnnotation': '', 'locationAnnotation': '',
                                   'stressValue': '', 'heartRateVariabilityValue': '',
                                   'date': beginDate.strftime("%d.%m.%y"), 'stressBalanceValue': '',
                                   'glucoseAnnotation': '', 'bgValue': '', 'exerciseTimeValue': (float(d['exerciseTimeValue']) - ((beginDate - dateParser(d['date'], d['time'])).total_seconds() / 60)), 'time': '00:00',
                                   'sleepAnnotation': '', 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                                   'otherAnnotation': ''}
                cacheValuesExist = True
        elif tempDate >= beginDate and tempDate < endDate:
            if cacheValuesExist:
                cacheValuesExist = False
                if locationCache:
                    datasubset.append(locationCache)
                if stressCache:
                    datasubset.append(stressCache)
                if sleepCache:
                    datasubset.append(sleepCache)
                if basalCache:
                    datasubset.append(basalCache)
                if exerciseCache:
                    datasubset.append(exerciseCache)
            datasubset.append(d)

    return datasubset

def findLimits(dataset, config):
    limits = {}
    limits['maxBarValue'] = config["limits"].getfloat("maxBarValue")
    limits['minCgmBgValue'] = config["limits"].getfloat("minCgmBgValue")
    limits['maxBasalValue'] = config["limits"].getfloat("maxBasalValue")
    limits['maxBasalBelowLegendValue'] = config["limits"].getfloat("maxBasalBelowLegendValue")
    limits['minHrValue'] = config["limits"].getfloat("minHrValue")
    limits['maxHrValue'] = config["limits"].getfloat("maxHrValue")

    if not config["limits"].getboolean("limitsManual"):
        for d in dataset:
            if d['bgValue']:
                if float(d['bgValue']) < limits['minCgmBgValue']:
                    limits['minCgmBgValue'] = float(d['bgValue'])
            if d['cgmValue']:
                if float(d['cgmValue']) < limits['minCgmBgValue']:
                    limits['minCgmBgValue'] = float(d['cgmValue'])
            if d['basalValue']:
                if float(d['basalValue']) > limits['maxBasalValue']:
                    if dateParser(d['date'], d['time']) < dateParser(d['date'], d['time']).replace(hour=04, minute=00):
                        limits['maxBasalBelowLegend'] = float(d['basalValue'])
                    limits['maxBasalValue'] = float(d['basalValue'])
            if d['bolusValue']:
                if float(d['bolusValue']) > limits['maxBarValue']:
                    limits['maxBarValue'] = float(d['bolusValue'])
            if d['mealValue']:
                if float(d['mealValue']) > limits['maxBarValue']:
                    limits['maxBarValue'] = float(d['mealValue'])
            if d['heartRateValue']:
                if float(d['heartRateValue']) < limits['minHrValue']:
                    limits['minHrValue'] = float(d['heartRateValue'])
                if float(d['heartRateValue']) > limits['maxHrValue']:
                    limits['maxHrValue'] = float(d['heartRateValue'])

    return limits

def linregressScipy(x, y):
    x = np.asarray(x)
    y = np.asarray(y)

    if x.size == 0 or y.size == 0:
        raise ValueError("[error] Inputs must not be empty.")

    xmean = np.mean(x, None)
    ymean = np.mean(y, None)

    # average sum of squares:
    ssxm, ssxym, ssyxm, ssym = np.cov(x, y, bias=1).flat
    r_num = ssxym

    slope = r_num / ssxm
    intercept = ymean - slope*xmean
    return (slope, intercept)

def parseSlices(csvFileName):
    with open(csvFileName, 'rU') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', dialect=csv.excel_tab)
        header = reader.next()

        if slicesDataFormat != header:
            print '[FormatError] File \'' + csvFileName + '\' has a wrong header'
            exit(0)

        slices = []

        for row in reader:
            slices.append(dict(zip(header, row)))

    return slices

rewindAvailable = False
katErrAvailable = False
exerciseAvailable = False
cgmCalibrationErrorAvailable = False
cgmConnectionErrorAvailable = False
cgmSensorFinishedAvailable = False
cgmSensorStartAvailable = False
cgmTimeSyncAvailable = False
pumpReservoirEmptyAvailable = False


def prepareDataset(dataset, config, beginDate, endDate):
    global rewindAvailable
    global katErrAvailable
    global exerciseAvailable
    global cgmCalibrationErrorAvailable
    global cgmConnectionErrorAvailable
    global cgmSensorFinishedAvailable
    global cgmSensorStartAvailable
    global cgmTimeSyncAvailable
    global pumpReservoirEmptyAvailable

    plottingData = {'basalValuesX':[], 'basalValuesY':[],
                    'mergedBolusX':[], 'mergedBolusY':[],
                    'mergedCarbX':[], 'mergedCarbY':[],
                    'bolusSquareValuesX':[], 'bolusSquareValuesY':[],
                    'sleepTouples':[],
                    'cgmValuesX':[[]], 'cgmValuesY':[[]],
                    'cgmValuesRedX': [], 'cgmValuesRedY': [],
                    'cgmAlertValuesX':[], 'cgmAlertValuesY':[],
                    'cgmAlertValuesRedX':[], 'cgmAlertValuesRedY':[],
                    'cgmRawValuesX':[[]], 'cgmRawValuesY':[[]],
                    'bgValuesX':[], 'bgValuesY':[],
                    'bolusCalculationValuesX':[], 'bolusCalculationValuesY':[],
                    'heartRateValuesX':[[]], 'heartRateValuesY':[[]],
                    'pumpRewindX':[], 'pumpRewindY':[],
                    'pumpKatErrX':[], 'pumpKatErrY':[],
                    'MarkerExerciseX':[], 'MarkerExerciseY':[],
                    'exerciseX':[], 'exerciseY':[], 'exerciseColor':[],
                    'stressX':[], 'stressY':[],
                    'locationX':[], 'locationY':[],
                    'mlCgmValuesX':[[]], 'mlCgmValuesY':[[]],
                    'pumpCgmPredictionValuesX': [[]], 'pumpCgmPredictionValuesY': [[]],
                    'cgmCalibrationValuesX':[], 'cgmCalibrationValuesY':[],
                    'MarkerCgmCalibrationErrorX': [], 'MarkerCgmCalibrationErrorY': [],
                    'MarkerCgmConnectionErrorX': [], 'MarkerCgmConnectionErrorY': [],
                    'MarkerCgmSensorFinishedX': [], 'MarkerCgmSensorFinishedY': [],
                    'MarkerCgmSensorStartX': [], 'MarkerCgmSensorStartY': [],
                    'MarkerCgmTimeSyncX': [], 'MarkerCgmTimeSyncY': [],
                    'MarkerPumpReservoirEmptyX': [], 'MarkerPumpReservoirEmptyY': [],
                    'suspendTouples':[],
                    'glucoseElevationValuesX':[], 'glucoseElevationValuesY':[], 'glucoseElevationValuesCGM':[],
                    'cgmValuesTotal':0, 'cgmValuesBelow':0, 'cgmValuesAbove':0, 'basalTotal':0, 'bolusTotal':0,
                    'carbTotal':0, 'autonomousSuspensionTotal':datetime.timedelta(minutes=0), 'noteAnnotation':"",
                    'cgmValuesInRange':0, 'cgmGenerics':{}, 'bolusCalculationGenerics':{}, 'bolusGenerics':{},
                    'basalGenerics':{}, 'symbolGenerics':{}}

    # Temp
    bolusValuesX = []
    bolusValuesY = []
    carbValuesX = []
    carbValuesY = []
    suspendTemp = []
    cgmIndex = 0
    cgmRawIndex = 0
    cgmMlIndex = 0
    pumpCgmPredictionIndex = 0
    hrIndex = 0
    prevCgmDate = ""
    prevCgmRawDate = ""
    prevCgmMlDate = ""
    prevHrDate = ""

    prevBasalValue = ""
    prevBasalDate = ""

    generics = []
    global genericsColors
    for g in config.items("generics"):
        if g[1]:
            tempString = '{"data": ' + g[1] +  '}'
            tempGenerics = json.loads(tempString)

            for n in tempGenerics["data"]:
                label = n[0]
                columnName = n[1]
                color = n[2]
                marker = n[3]

                if g[0] == "cgm":
                    if color:
                        genericsColors['cgmGenerics'][columnName] = color
                    plottingData['cgmGenerics'][columnName] = {'X':[],'Y':[],'color':genericsColors['cgmGenerics'][columnName],'label':label,'marker':marker}
                if g[0] == "boluscalculation":
                    if color:
                        genericsColors['bolusCalculationGenerics'][columnName] = color
                    plottingData['bolusCalculationGenerics'][columnName] = {'X':[],'Y':[],'color':genericsColors['bolusCalculationGenerics'][columnName],'label':label,'marker':marker}
                if g[0] == "bolus":
                    if color:
                        genericsColors['bolusGenerics'][columnName] = color
                    plottingData['bolusGenerics'][columnName] = {'X':[],'Y':[],'color':genericsColors['bolusGenerics'][columnName],'label':label,'marker':marker}
                if g[0] == "basal":
                    if color:
                        genericsColors['basalGenerics'][columnName] = color
                    plottingData['basalGenerics'][columnName] = {'X':[],'Y':[],'color':genericsColors['basalGenerics'][columnName],'label':label,'marker':marker}
                if g[0] == "symbol":
                    if color:
                        genericsColors['symbolGenerics'][columnName] = color
                    plottingData['symbolGenerics'][columnName] = {'X':[],'Y':[],'color':genericsColors['symbolGenerics'][columnName],'label':label,'marker':marker}
        generics.append(g[0])

    for d in dataset:
        tempDate = dateParser(d['date'], d['time'])
        if d['basalValue']:
            if prevBasalValue and prevBasalDate:
                plottingData['basalTotal'] += (((tempDate - prevBasalDate).total_seconds() / 3600) * float(prevBasalValue))
            prevBasalValue = d['basalValue']
            prevBasalDate = tempDate
            plottingData['basalValuesX'].append(tempDate)
            plottingData['basalValuesY'].append(d['basalValue'])
        if d['bolusValue']:
            plottingData['bolusTotal'] += float(d['bolusValue'])
            if "BOLUS_SQARE" not in d['bolusAnnotation']:
                bolusValuesX.append(tempDate)
                bolusValuesY.append(d['bolusValue'])
            else:
                for a in re.split(config["axisLabels"].get("delimiter"), d['bolusAnnotation']):
                    if "BOLUS_SQARE" in a:
                        plottingData['bolusSquareValuesX'].append(tempDate)
                        plottingData['bolusSquareValuesY'].append((d['bolusValue'], re.split("=", a)[1]))
        if d['mealValue']:
            carbValuesX.append(tempDate)
            carbValuesY.append(d['mealValue'])
            plottingData['carbTotal'] += float(d['mealValue'])
        if d['sleepAnnotation']:
            if d['sleepAnnotation'] == "SLEEP_DEEP":
                plottingData['sleepTouples'].append((tempDate, tempDate + datetime.timedelta(minutes=float(d['sleepValue'])), config["colors"].get("deepSleepColor")))
            if d['sleepAnnotation'] == "SLEEP_LIGHT":
                plottingData['sleepTouples'].append((tempDate, tempDate + datetime.timedelta(minutes=float(d['sleepValue'])), config["colors"].get("lightSleepColor")))
        if d['cgmValue']:
            if prevCgmDate and ((tempDate - prevCgmDate) > datetime.timedelta(minutes=config["limits"].getfloat("interruptLinePlotMinutes"))):
                cgmIndex += 1
                plottingData['cgmValuesX'].append([])
                plottingData['cgmValuesY'].append([])
            prevCgmDate = tempDate
            plottingData['cgmValuesX'][cgmIndex].append(tempDate)
            if float(d['cgmValue']) > config["limits"].getfloat("bgCgmMaxValue"):
                plottingData['cgmValuesY'][cgmIndex].append(config["limits"].getfloat("cgmBgHighLimit") - 1)
                plottingData['cgmValuesRedX'].append(tempDate)
                plottingData['cgmValuesRedY'].append(config["limits"].getfloat("cgmBgHighLimit") - 1)
            else:
                plottingData['cgmValuesY'][cgmIndex].append(d['cgmValue'])
        if d['cgmAlertValue']:
            plottingData['cgmAlertValuesX'].append(tempDate)
            if float(d['cgmAlertValue']) > config["limits"].getfloat("bgCgmMaxValue"):
                plottingData['cgmAlertValuesY'].append(config["limits"].getfloat("cgmBgHighLimit") - 1)
                plottingData['cgmAlertValuesRedX'].append(tempDate)
                plottingData['cgmAlertValuesRedY'].append(config["limits"].getfloat("cgmBgHighLimit") - 1)
            else:
                plottingData['cgmAlertValuesY'].append(d['cgmAlertValue'])
        if d['cgmRawValue']:
            if prevCgmRawDate and ((tempDate - prevCgmRawDate) > datetime.timedelta(minutes=config["limits"].getfloat("interruptLinePlotMinutes"))):
                cgmRawIndex += 1
                plottingData['cgmRawValuesX'].append([])
                plottingData['cgmRawValuesY'].append([])
            prevCgmRawDate = tempDate
            plottingData['cgmRawValuesX'][cgmRawIndex].append(tempDate)
            plottingData['cgmRawValuesY'][cgmRawIndex].append(d['cgmRawValue'])
        if d['bgValue']:
            if not(d['glucoseAnnotation'] == "GLUCOSE_BG_MANUAL"):
                plottingData['bgValuesX'].append(tempDate)
                if float(d['bgValue']) > config["limits"].getfloat("bgCgmMaxValue"):
                    plottingData['bgValuesY'].append(config["limits"].getfloat("cgmBgHighLimit") - 1)
                else:
                    plottingData['bgValuesY'].append(d['bgValue'])
        if d['glucoseAnnotation']:
            annotations = re.split(config["axisLabels"].get("delimiter"), d['glucoseAnnotation'])
            for a in annotations:
                if "CGM_CALIBRATION_ERROR" in a:
                    plottingData['MarkerCgmCalibrationErrorX'].append(tempDate)
                    plottingData['MarkerCgmCalibrationErrorY'].append(1)
                    cgmIndex += 1
                    plottingData['cgmValuesX'].append([])
                    plottingData['cgmValuesY'].append([])
                    cgmCalibrationErrorAvailable = True
                if "CGM_CONNECTION_ERROR" in a:
                    plottingData['MarkerCgmConnectionErrorX'].append(tempDate)
                    plottingData['MarkerCgmConnectionErrorY'].append(1)
                    cgmIndex += 1
                    plottingData['cgmValuesX'].append([])
                    plottingData['cgmValuesY'].append([])
                    cgmConnectionErrorAvailable = True
                if "CGM_SENSOR_FINISHED" in a:
                    plottingData['MarkerCgmSensorFinishedX'].append(tempDate)
                    plottingData['MarkerCgmSensorFinishedY'].append(1)
                    cgmIndex += 1
                    plottingData['cgmValuesX'].append([])
                    plottingData['cgmValuesY'].append([])
                    cgmSensorFinishedAvailable = True
                if "CGM_SENSOR_START" in a:
                    plottingData['MarkerCgmSensorStartX'].append(tempDate)
                    plottingData['MarkerCgmSensorStartY'].append(1)
                    cgmSensorStartAvailable = True
                if "CGM_TIME_SYNC" in a:
                    plottingData['MarkerCgmTimeSyncX'].append(tempDate)
                    plottingData['MarkerCgmTimeSyncY'].append(1)
                    cgmTimeSyncAvailable = True
                if "GLUCOSE_CGM_CALIBRATION" in a and not "CGM_CALIBRATION_ERROR" in a:
                    plottingData['cgmCalibrationValuesX'].append(tempDate)
                    plottingData['cgmCalibrationValuesY'].append(re.split("=", a)[1])
                if "GLUCOSE_ELEVATION_30" in a:
                    plottingData['glucoseElevationValuesX'].append(tempDate)
                    plottingData['glucoseElevationValuesY'].append(re.split("=", a)[1])
                    plottingData['glucoseElevationValuesCGM'].append(d['cgmValue'])
        if d['bolusCalculationValue']:
            plottingData['bolusCalculationValuesX'].append(tempDate)
            plottingData['bolusCalculationValuesY'].append(d['bolusCalculationValue'])
        if d['heartRateValue']:
            if prevHrDate and ((tempDate - prevHrDate) > datetime.timedelta(minutes=config["limits"].getfloat("interruptLinePlotMinutes"))):
                hrIndex += 1
                plottingData['heartRateValuesX'].append([])
                plottingData['heartRateValuesY'].append([])
            prevHrDate = tempDate
            plottingData['heartRateValuesX'][hrIndex].append(tempDate)
            plottingData['heartRateValuesY'][hrIndex].append(d['heartRateValue'])
        if d['pumpAnnotation']:
            if "PUMP_FILL" in d['pumpAnnotation']:
                plottingData['pumpRewindX'].append(tempDate)
                plottingData['pumpRewindY'].append(1)
                rewindAvailable = True
            if "PUMP_KATERR" in d['pumpAnnotation']:
                plottingData['pumpKatErrX'].append(tempDate)
                plottingData['pumpKatErrY'].append(1)
                katErrAvailable = True
            if "PUMP_RESERVOIR_EMPTY" in d['pumpAnnotation']:
                plottingData['MarkerPumpReservoirEmptyX'].append(tempDate)
                plottingData['MarkerPumpReservoirEmptyY'].append(1)
                pumpReservoirEmptyAvailable = True
            if "PUMP_AUTONOMOUS_SUSPEND" in d['pumpAnnotation']:
                suspendTemp = tempDate
            if "PUMP_UNSUSPEND" in d['pumpAnnotation']:
                if suspendTemp:
                    plottingData['suspendTouples'].append((suspendTemp,tempDate))
                    suspendTemp = []
        if d['exerciseTimeValue']:
            if d['exerciseAnnotation']:
                doAppend = False
                annotations = re.split(config["axisLabels"].get("delimiter"), d['exerciseAnnotation'])
                if "EXERCISE_LOW" in annotations:
                    plottingData['exerciseColor'].append(config["colors"].get("exerciseLowColor"))
                    doAppend = True
                elif "EXERCISE_MID" in annotations:
                    plottingData['exerciseColor'].append(config["colors"].get("exerciseMidColor"))
                    doAppend = True
                elif "EXERCISE_HIGH" in annotations:
                    plottingData['exerciseColor'].append(config["colors"].get("exerciseHighColor"))
                    doAppend = True
                if doAppend:
                    plottingData['exerciseX'].append(tempDate)
                    plottingData['exerciseY'].append(d['exerciseTimeValue'])
        elif d['exerciseAnnotation']:
            plottingData['MarkerExerciseX'].append(tempDate)
            plottingData['MarkerExerciseY'].append(1)
            exerciseAvailable = True
        if d['stressValue']:
            plottingData['stressX'].append(tempDate)
            plottingData['stressY'].append(d['stressValue'])
        if d['locationAnnotation']:
            plottingData['locationX'].append(tempDate)
            plottingData['locationY'].append(d['locationAnnotation'])
        if d['mlCgmValue']:
            tempValue = ""
            if len(re.split(":", d['mlCgmValue'])) <= config["limits"].getint("mlCgmArrayIndex"):
                tempValue = re.split(":", d['mlCgmValue'])[0]
            else:
                tempValue = re.split(":", d['mlCgmValue'])[config["limits"].getint("mlCgmArrayIndex")]
            if prevCgmMlDate and ((tempDate - prevCgmMlDate) > datetime.timedelta(minutes=config["limits"].getfloat("interruptLinePlotMinutes"))):
                cgmMlIndex += 1
                plottingData['mlCgmValuesX'].append([])
                plottingData['mlCgmValuesY'].append([])
            prevCgmMlDate = tempDate
            plottingData['mlCgmValuesX'][cgmMlIndex].append(tempDate)
            plottingData['mlCgmValuesY'][cgmMlIndex].append(tempValue)
        if d['pumpCgmPredictionValue']:
            if prevCgmMlDate and ((tempDate - prevCgmMlDate) > datetime.timedelta(minutes=config["limits"].getfloat("interruptLinePlotMinutes"))):
                pumpCgmPredictionIndex += 1
                plottingData['pumpCgmPredictionValuesX'].append([])
                plottingData['pumpCgmPredictionValuesY'].append([])
            prevCgmMlDate = tempDate
            plottingData['pumpCgmPredictionValuesX'][pumpCgmPredictionIndex].append(tempDate)
            plottingData['pumpCgmPredictionValuesY'][pumpCgmPredictionIndex].append(d['pumpCgmPredictionValue'])
        if d['otherAnnotation']:
            annotations = re.split(config["axisLabels"].get("delimiter"), d['otherAnnotation'])
            for a in annotations:
                if re.split("=", a)[0] == "USER_TEXT":
                    noteLength = 0
                    for s in re.split("=", a)[1].split():
                        noteLength += len(s) + 1
                        if noteLength > config["dailyStatistics"].getint("maxLengthNotes"):
                            plottingData['noteAnnotation'] += "..."
                            break
                        else:
                            plottingData['noteAnnotation'] += " "
                            plottingData['noteAnnotation'] += s
        for g in generics:
            tempString = '{"data": ' + config["generics"].get(g) + '}'
            tempGenerics = json.loads(tempString)

            for n in tempGenerics["data"]:
                label = n[0]
                columnName = n[1]
                color = n[2]
                marker = n[3]

                if g == "cgm":
                    try:
                        if d[columnName]:
                            plottingData['cgmGenerics'][columnName]['Y'].append(d[columnName])
                            plottingData['cgmGenerics'][columnName]['X'].append(tempDate)
                    except Exception, e:
                        pass
                if g == "boluscalculation":
                    try:
                        if d[columnName]:
                            plottingData['bolusCalculationGenerics'][columnName]['Y'].append(d[columnName])
                            plottingData['bolusCalculationGenerics'][columnName]['X'].append(tempDate)
                    except Exception, e:
                        pass
                if g == "bolus":
                    try:
                        if d[columnName]:
                            plottingData['bolusGenerics'][columnName]['Y'].append(d[columnName])
                            plottingData['bolusGenerics'][columnName]['X'].append(tempDate)
                    except Exception, e:
                        pass
                if g == "basal":
                    try:
                        if d[columnName]:
                            plottingData['basalGenerics'][columnName]['Y'].append(d[columnName])
                            plottingData['basalGenerics'][columnName]['X'].append(tempDate)
                    except Exception, e:
                        pass
                if g == "symbol":
                    try:
                        if d[columnName]:
                            plottingData['symbolGenerics'][columnName]['Y'].append(1)
                            plottingData['symbolGenerics'][columnName]['X'].append(tempDate)
                    except Exception, e:
                        pass


    # extend basal to the end of the plot
    if plottingData['basalValuesY']:
        plottingData['basalValuesX'].append(endDate)
        plottingData['basalValuesY'].append(plottingData['basalValuesY'][len(plottingData['basalValuesY']) - 1])

    ### Merge barplot values ###
    mergeTimespan = 15
    dateI = beginDate
    currentMerge = 0.0
    currentCount = 0
    for i in range(0, 96):
        ## merge bolus ##
        for i, t in enumerate(bolusValuesX):
            if t >= dateI and t < (dateI + datetime.timedelta(minutes=mergeTimespan)):
                currentMerge += float(bolusValuesY[i])
                currentCount += 1
        if currentCount > 0:
            plottingData['mergedBolusX'].append(dates.date2num(dateI))
            plottingData['mergedBolusY'].append(currentMerge / currentCount)
        currentMerge = 0.0
        currentCount = 0

        ## merge carb ##
        for i, t in enumerate(carbValuesX):
            if t >= dateI and t < (dateI + datetime.timedelta(minutes=mergeTimespan)):
                currentMerge += float(carbValuesY[i])
                currentCount += 1
        if currentCount > 0:
            plottingData['mergedCarbX'].append(dates.date2num(dateI))
            plottingData['mergedCarbY'].append(currentMerge / currentCount)
        currentMerge = 0.0
        currentCount = 0
        dateI = dateI + datetime.timedelta(minutes=mergeTimespan)


    ### daily notes calcs ###
    plottingData['cgmValuesTotal'] = 0
    plottingData['cgmValuesBelow'] = 0
    plottingData['cgmValuesAbove'] = 0
    plottingData['cgmValuesInRange'] = 0
    for i in range(0, len(plottingData['cgmValuesX'])):
        plottingData['cgmValuesTotal'] += len(plottingData['cgmValuesX'][i])
        for j in range(0, len(plottingData['cgmValuesX'][i])):
            if float(plottingData['cgmValuesY'][i][j]) > config["limits"].getfloat("cgmBgLimitMarkerHigh"):
                plottingData['cgmValuesAbove'] += 1
            if float(plottingData['cgmValuesY'][i][j]) < config["limits"].getfloat("cgmBgLimitMarkerLow"):
                plottingData['cgmValuesBelow'] += 1
            if float(plottingData['cgmValuesY'][i][j]) > config["limits"].getfloat("hmin") and float(plottingData['cgmValuesY'][i][j]) < config["limits"].getfloat("hmax"):
                plottingData['cgmValuesInRange'] += 1
    for t in plottingData['suspendTouples']:
        plottingData['autonomousSuspensionTotal'] += (t[1] - t[0])

    # basal last step
    if prevBasalValue and prevBasalDate:
        plottingData['basalTotal'] += (((endDate - prevBasalDate + datetime.timedelta(seconds=1)).total_seconds() / 3600) * float(prevBasalValue))

    return plottingData

dayCounter = {}
plotCounter = {"SLICE_DAILYSTATISTICS":0, "SLICE_DAILY":0, "SLICE_TINY":0, "SLICE_NORMAL":0, "SLICE_BIG":0}

# plotType: might be SLICE_TINY, SLICE_NORMAL, SLICE_BIG or SLICE_DAILY

def plot(dataset, config, outPath, beginDate, duration, plotType, extLegend, limits, dailyNotes):
    plt.close()
    global dayCounter
    global plotCounter

    datasubset = dataSubset(dataset, beginDate, duration)
    endDate = beginDate + datetime.timedelta(minutes=duration) - datetime.timedelta(seconds=1.0)
    plottingData = prepareDataset(datasubset, config, beginDate, endDate)

    if config["plotBooleans"].getboolean("plotElevation"):
        ### glucose elevation ###
        elevationSliceCount = 12
        if plotType == "SLICE_DAILY" or plotType == "SLICE_BIG":
            elevationSliceCount = 12
        elif plotType == "SlICE_NORMAL":
            elevationSliceCount = 6
        elif plotType == "SLICE_TINY":
            elevationSliceCount = 4
        elevationSliceTimespan = (endDate-beginDate) / elevationSliceCount

        elevationSliceCounter = 0
        elevationTempDate = beginDate

        elevationSlices = []
        elevationSlicesCGM = []
        elevationSlicesMax = []
        elevationSlicesMaxCGM = []
        for i in range(0,elevationSliceCount):
            elevationSlices.append([])
            elevationSlicesCGM.append([])
            elevationSlicesMax.append("")
            elevationSlicesMaxCGM.append("")
        for i in range(0,len(plottingData['glucoseElevationValuesX'])):
            while not (plottingData['glucoseElevationValuesX'][i] >= elevationTempDate and plottingData['glucoseElevationValuesX'][i] <= (elevationTempDate+elevationSliceTimespan)):
                elevationSliceCounter += 1
                elevationTempDate += elevationSliceTimespan
            elevationSlices[elevationSliceCounter].append((plottingData['glucoseElevationValuesX'][i],plottingData['glucoseElevationValuesY'][i]))
            elevationSlicesCGM[elevationSliceCounter].append(plottingData['glucoseElevationValuesCGM'][i])

        for i in range(0,len(elevationSlices)):
            if len(elevationSlices[i]) > 0:
                maxDate = elevationSlices[i][0][0]
                maxElevationValue = float(elevationSlices[i][0][1])
                maxCGMValue = float(elevationSlicesCGM[i][0])
                for j in range(1,len(elevationSlices[i])):
                    if abs(maxElevationValue) < abs(float(elevationSlices[i][j][1])):
                        maxElevationValue = float(elevationSlices[i][j][1])
                        maxDate = elevationSlices[i][j][0]
                        maxCGMValue = float(elevationSlicesCGM[i][j])
                    elevationSlicesMaxCGM[i] = float(elevationSlicesCGM[i][j])
                middleValue = maxElevationValue
                for d in dataSubset(dataset, maxDate - datetime.timedelta(minutes = 30), 30.0):
                    if d['cgmValue']:
                        middleValue = (maxCGMValue + float(d['cgmValue'])) / 2
                        break

                elevationSlicesMax[i] = (maxElevationValue, middleValue, (maxDate - datetime.timedelta(minutes = 15)))
        for i in range(1,len(elevationSlicesMax)):
            if elevationSlicesMax[i-1] != "" and elevationSlicesMax[i] != "":
                if elevationSlicesMax[i-1][2] < (beginDate + datetime.timedelta(minutes=20)):
                    elevationSlicesMax[i-1] = (elevationSlicesMax[i-1][0], elevationSlicesMax[i-1][1], (beginDate + datetime.timedelta(minutes=20)))
                if elevationSlicesMax[i][2] > (endDate - datetime.timedelta(minutes=20)):
                    elevationSlicesMax[i] = (elevationSlicesMax[i][0], elevationSlicesMax[i][1], (endDate - datetime.timedelta(minutes=20)))
                if (elevationSlicesMax[i][2] - elevationSlicesMax[i-1][2]) <= datetime.timedelta(minutes = 30):
                    if elevationSlicesMax[i-1][0] > elevationSlicesMax[i][0]:
                        elevationSlicesMax[i] = ""
                    else:
                        elevationSlicesMax[i-1] = ""

    ########## Prepare Canvas ##########
    plt.rcParams.update({'font.size': 12})
    plt.rcParams.update({'font.sans-serif': ['Verdana']})
    plt.rcParams.update({'font.family': 'sans-serif'})
    fig = plt.figure()

    ### row spans ###
    # axBarPlots    380
    # axSleep       476
    # axStress       36
    # axExercise     36
    # axLocation     36
    # axSymbols      36

    currentIndex = 0

    if config['plotBooleans'].getboolean("plotSymbols"):
        axSymbolsIndex = currentIndex
        currentIndex += 36
    if config['plotBooleans'].getboolean("plotLocation"):
        axLocationIndex = currentIndex
        currentIndex += 36
    if config['plotBooleans'].getboolean("plotExercise"):
        axExerciseIndex = currentIndex
        currentIndex += 36
    if config['plotBooleans'].getboolean("plotStress"):
        axStressIndex = currentIndex
        currentIndex += 36
    if config['plotBooleans'].getboolean("plotSleep") or config['plotBooleans'].getboolean("plotBg") or config['plotBooleans'].getboolean("plotCgm") or config['plotBooleans'].getboolean("plotCgmRaw") or config['plotBooleans'].getboolean("plotBolusCalculation") or config['plotBooleans'].getboolean("plotHeartRate") or config['plotBooleans'].getboolean("plotElevation"):
        axSleepIndex = currentIndex
        currentIndex += 476

    axBarPlotsIndex = currentIndex
    currentIndex += 380

    al = 7
    arrowprops = dict(clip_on=True, headlength=al, headwidth=al, facecolor='k')
    kwargs = dict(xycoords='axes fraction', textcoords='offset points', arrowprops=arrowprops)

    axBarPlots = plt.subplot2grid((currentIndex, 1), (axBarPlotsIndex, 0), rowspan=380, colspan=1)
    if config['plotBooleans'].getboolean("plotSleep") or config['plotBooleans'].getboolean("plotBg") or config['plotBooleans'].getboolean("plotCgm") or config['plotBooleans'].getboolean("plotCgmRaw") or config['plotBooleans'].getboolean("plotBolusCalculation") or config['plotBooleans'].getboolean("plotHeartRate") or config['plotBooleans'].getboolean("plotElevation"):
        axSleep = plt.subplot2grid((currentIndex, 1), (axSleepIndex, 0), rowspan=476, colspan=1, sharex=axBarPlots)
        for tic in axSleep.xaxis.get_major_ticks():
            tic.tick1On = tic.tick2On = False
            tic.label1On = tic.label2On = False
        axSleep.grid(color=config["colors"].get("gridColor"), linestyle='-', which='both')

        if axBarPlotsIndex == 0:
            axSleep.annotate("", (0, 1), xytext=(0, -al), **kwargs)
            axSleep.annotate("", (1, 1), xytext=(0, -al), **kwargs)
    if config['plotBooleans'].getboolean("plotStress"):
        axStress = plt.subplot2grid((currentIndex, 1), (axStressIndex, 0), rowspan=36, colspan=1, sharex=axBarPlots)
        for tic in axStress.xaxis.get_major_ticks():
            tic.tick1On = tic.tick2On = False
            tic.label1On = tic.label2On = False
        axStress.yaxis.set_visible(False)

        if axStressIndex == 0:
            axStress.annotate("", (0, 1), xytext=(0, -al), **kwargs)
            axStress.annotate("", (1, 1), xytext=(0, -al), **kwargs)
    if config['plotBooleans'].getboolean("plotExercise"):
        axExercise = plt.subplot2grid((currentIndex, 1), (axExerciseIndex, 0), rowspan=36, colspan=1, sharex=axBarPlots)
        for tic in axExercise.xaxis.get_major_ticks():
            tic.tick1On = tic.tick2On = False
            tic.label1On = tic.label2On = False
        axExercise.yaxis.set_visible(False)

        if axExerciseIndex == 0:
            axExercise.annotate("", (0, 1), xytext=(0, -al), **kwargs)
            axExercise.annotate("", (1, 1), xytext=(0, -al), **kwargs)
    if config['plotBooleans'].getboolean("plotLocation"):
        axLocation = plt.subplot2grid((currentIndex, 1), (axLocationIndex, 0), rowspan=36, colspan=1, sharex=axBarPlots)
        for tic in axLocation.xaxis.get_major_ticks():
            tic.tick1On = tic.tick2On = False
            tic.label1On = tic.label2On = False
        axLocation.yaxis.set_visible(False)

        if axLocationIndex == 0:
            axLocation.annotate("", (0, 1), xytext=(0, -al), **kwargs)
            axLocation.annotate("", (1, 1), xytext=(0, -al), **kwargs)
    if config['plotBooleans'].getboolean("plotSymbols"):
        axSymbols = plt.subplot2grid((currentIndex, 1), (axSymbolsIndex, 0), rowspan=36, colspan=1, sharex=axBarPlots)
        for tic in axSymbols.xaxis.get_major_ticks():
            tic.tick1On = tic.tick2On = False
            tic.label1On = tic.label2On = False
        axSymbols.yaxis.set_visible(False)
        axSymbols.grid(color=config["colors"].get("gridColor"), linestyle='-')
        axSymbols.set_axisbelow(True)

        axSymbols.annotate("", (0, 1), xytext=(0, -al), **kwargs)
        axSymbols.annotate("", (1, 1), xytext=(0, -al), **kwargs)

    if config["plotBooleans"].getboolean("plotBasal"):
        axBasal = axBarPlots.twinx()
    axCgmBg = axSleep.twinx()

    axBarPlots.grid(color=config["colors"].get("gridColor"), linestyle='-', zorder=1)
    axBarPlots.set_axisbelow(True)

    axBarPlots.xaxis_date()
    axBarPlots.xaxis.set_major_formatter(dates.DateFormatter('%H:%M'))
    axBarPlots.xaxis.set_major_locator(dates.HourLocator(interval=2))

    if (config["axisLabels"].getboolean("showXaxisLabel")):
        axBarPlots.set_xlabel(config["axisLabels"].get("xaxisLabel"))
        axBarPlots.xaxis.set_label_position('bottom')

    # delete space between the subplots to make it look like one
    fig.subplots_adjust(hspace=0)
    ########## Prepare Canvas ##########


    ########## Basal, Bolus, Carb Plot ##########
    if config["plotBooleans"].getboolean("plotAutonomousSuspend"):
        for d in plottingData['suspendTouples']:
            axBarPlots.axvspan(d[0], d[1], facecolor=config["colors"].get("autonomousSuspendColor"), zorder=0)
    if config["plotBooleans"].getboolean("plotBasal"):
        gridSteps = 6
        barStepSize = 0.5

        if limits['maxBarValue'] >= 6:
            barStepSize = round((limits['maxBarValue']/gridSteps)*2)/2
            if barStepSize * gridSteps < limits['maxBarValue']:
                gridSteps += 1
        elif limits['maxBarValue'] >= 3:
            barStepSize = 1

        barPlotsMax = gridSteps * barStepSize

        axBarPlots.set_ylim(0, barPlotsMax)
        axBarPlots.yaxis.set_major_locator(MultipleLocator(barStepSize))


        basalStepSize = 0.1

        if limits['maxBasalValue'] > 2.5:
            basalStepSize = round(limits['maxBasalValue']/5)
        elif limits['maxBasalValue'] > 1:
            basalStepSize = 0.5
        elif limits['maxBasalValue'] > 0.5:
            basalStepSize = 0.2

        basalMax = gridSteps * basalStepSize

        axBasal.set_ylim(0, basalMax)
        axBasal.yaxis.set_major_locator(MultipleLocator(basalStepSize))
        basalPlot, = axBasal.plot(plottingData['basalValuesX'], plottingData['basalValuesY'], linewidth=config["linewidths"].getfloat("basalLineWidth"), color=config["colors"].get("basalPlotColor"), drawstyle='steps-post')  # Plot basalValues

        # basal generic plot
        for cell in plottingData['basalGenerics']:
            basalGenericPlot, = axBasal.plot(plottingData['basalGenerics'][cell]['X'],
                                             plottingData['basalGenerics'][cell]['Y'],
                                             linewidth=config["linewidths"].getfloat("basalLineWidth"),
                                             color=plottingData['basalGenerics'][cell]['color'],
                                             drawstyle='steps-post')  # Plot basalValues

        axBasal.set_ylabel(config["axisLabels"].get("basalLabel"))

        axBasal.spines['top'].set_visible(False)

    if config["plotBooleans"].getboolean("plotCarb"):
        carbBars = axBarPlots.bar([dates.num2date(t) - datetime.timedelta(minutes=3) for t in plottingData['mergedCarbX']], plottingData['mergedCarbY'], config["limits"].getfloat("barWidth"), color=config["colors"].get("carbBarColor"))
    if config["plotBooleans"].getboolean("plotBolus"):
        bolusBars = axBarPlots.bar([dates.num2date(t) + datetime.timedelta(minutes=3) for t in plottingData['mergedBolusX']], plottingData['mergedBolusY'], config["limits"].getfloat("barWidth"), color=config["colors"].get("bolusBarColor"))

        for cell in plottingData['bolusGenerics']:
            bolusGenericBars = axBarPlots.bar(
                plottingData['bolusGenerics'][cell]['X'],
                plottingData['bolusGenerics'][cell]['Y'], config["limits"].getfloat("barWidth"),
                color=plottingData['bolusGenerics'][cell]['color'])

        for i in range(0,len(plottingData['bolusSquareValuesX'])):
            tempWidth = dates.date2num(plottingData['bolusSquareValuesX'][i] + datetime.timedelta(seconds=float(plottingData['bolusSquareValuesY'][i][1]))) - dates.date2num(plottingData['bolusSquareValuesX'][i])
            l = axBarPlots.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['bolusSquareValuesX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    plottingData['bolusSquareValuesY'][i][0],  # height
                    edgecolor=config["colors"].get("bolusBarColor"),
                    facecolor=config["colors"].get("bolusBarColor"),
                    alpha=0.7
                )
            )

    if config["plotBooleans"].getboolean("plotCarb") or config["plotBooleans"].getboolean("plotBolus"):
        axBarPlots.set_ylabel(config["axisLabels"].get("bolusLabel"))

        axBarPlots.spines['top'].set_visible(False)
        axBarPlots.xaxis.set_ticks_position('bottom')
    ########## Basal, Bolus, Carb Plot ##########




    ########## CGM, BG, HR PLOT ##########
    minCgmBg = int((limits['minCgmBgValue'] / 25)) * 25
    gridOffset = (50 - minCgmBg) / 25
    axSleep.set_ylim(minCgmBg, config["limits"].getfloat("cgmBgHighLimit"))

    # background field specified by hmin/hmax
    # dummy axis for axhspan
    axCgmBg.set_ylim(minCgmBg, config["limits"].getfloat("cgmBgHighLimit"))
    axCgmBg.axes.get_yaxis().set_visible(False)
    # plot sleep data
    if config["plotBooleans"].getboolean("plotSleep"):
        for d in plottingData['sleepTouples']:
            axSleep.axvspan(d[0], d[1], facecolor=d[2], alpha=1.0)

    if config["plotBooleans"].getboolean("plotCgm") or config["plotBooleans"].getboolean("plotBg"):
        axSleep.axhspan(config["limits"].getfloat("hmin"), config["limits"].getfloat("hmax"), facecolor=config["colors"].get("hbgColor"), alpha=0.5, edgecolor='#8eb496')
        axCgmBg.axhspan(config["limits"].getfloat("cgmBgLimitMarkerLow"), config["limits"].getfloat("cgmBgLimitMarkerLow"),
                        facecolor=config["colors"].get("cgmBgLimitMarkerColor"),
                        alpha=0.5, edgecolor='#000000')
        axCgmBg.axhspan(config["limits"].getfloat("cgmBgLimitMarkerHigh"), config["limits"].getfloat("cgmBgLimitMarkerHigh"),
                        facecolor=config["colors"].get("cgmBgLimitMarkerColor"),
                        alpha=0.5, edgecolor='#000000')
        # turn off ticks where there is no spine
        axSleep.yaxis.set_ticks_position('left')
        axSleep.set_ylabel(config["axisLabels"].get("bgLabel"))

    if config["plotBooleans"].getboolean("plotCgm"):
        cgmRedHighValuesX = [[]]
        cgmRedHighValuesY = [[]]
        cgmRedLowValuesX = [[]]
        cgmRedLowValuesY = [[]]

        cgmRedHighIndex = 0
        cgmRedLowIndex = 0

        cgmRedHighLimit = float(config["limits"].getfloat("cgmBgLimitMarkerHigh"))
        cgmRedLowLimit = float(config["limits"].getfloat("cgmBgLimitMarkerLow"))


        # cgm generics
        for cell in plottingData['cgmGenerics']:
            cgmGenericPlot, = axCgmBg.plot(plottingData['cgmGenerics'][cell]['X'], plottingData['cgmGenerics'][cell]['Y'],
                                    marker=config["plotMarkers"].get("cgmMarker"),
                                    markersize=config["plotMarkers"].get("cgmMainMarkerSize"),
                                    linewidth=config["linewidths"].getfloat("cgmLineWidth"),
                                    color=plottingData['cgmGenerics'][cell]['color'])  # Plot cgmValues

        for i in range(0,len(plottingData['pumpCgmPredictionValuesX'])):
            pumpCgmPredictionPlot, = axCgmBg.plot(plottingData['pumpCgmPredictionValuesX'][i], plottingData['pumpCgmPredictionValuesY'][i], marker=config["plotMarkers"].get("cgmMarker"), markersize=config["plotMarkers"].get("cgmAdditionalMarkerSize"),
                                    linewidth=config["linewidths"].getfloat("pumpCgmPredictionLineWidth"), color=config["colors"].get("pumpCgmPredictionPlotColor"))

        for i in range(0,len(plottingData['cgmValuesX'])):
            cgmPlot, = axCgmBg.plot(plottingData['cgmValuesX'][i], plottingData['cgmValuesY'][i], marker=config["plotMarkers"].get("cgmMarker"), markersize=config["plotMarkers"].get("cgmMainMarkerSize"),
                            linewidth=config["linewidths"].getfloat("cgmLineWidth"), color=config["colors"].get("cgmPlotColor"))  # Plot cgmValues

            prevPoint = (0.0,0.0)

            for j in range(0, len(plottingData['cgmValuesY'][i])):
                ### HIGH LIMIT ###
                if ((float(plottingData['cgmValuesY'][i][j]) > cgmRedHighLimit and float(prevPoint[1]) <= cgmRedHighLimit) or (float(plottingData['cgmValuesY'][i][j]) < cgmRedHighLimit and float(prevPoint[1]) >= cgmRedHighLimit)) and prevPoint[0] != 0.0:
                    x1 = dates.date2num(prevPoint[0])
                    y1 = float(prevPoint[1])
                    x2 = dates.date2num(plottingData['cgmValuesX'][i][j])
                    y2 = float(plottingData['cgmValuesY'][i][j])

                    slope, intercept = linregressScipy([x1, x2], [y1, y2])

                    cgmRedHighValuesX[cgmRedHighIndex].append(dates.num2date(np.linalg.solve([[slope]], [cgmRedHighLimit - intercept])[0]))
                    cgmRedHighValuesY[cgmRedHighIndex].append(cgmRedHighLimit)
                if float(plottingData['cgmValuesY'][i][j]) > cgmRedHighLimit:
                    cgmRedHighValuesX[cgmRedHighIndex].append(plottingData['cgmValuesX'][i][j])
                    cgmRedHighValuesY[cgmRedHighIndex].append(plottingData['cgmValuesY'][i][j])
                if float(plottingData['cgmValuesY'][i][j]) < cgmRedHighLimit and float(prevPoint[1]) >= cgmRedHighLimit:
                    cgmRedHighValuesX.append([])
                    cgmRedHighValuesY.append([])
                    cgmRedHighIndex += 1

                ### LOW LIMIT ###
                if ((float(plottingData['cgmValuesY'][i][j]) <= cgmRedLowLimit and float(prevPoint[1]) > cgmRedLowLimit) or (float(plottingData['cgmValuesY'][i][j]) >= cgmRedLowLimit and float(prevPoint[1]) < cgmRedLowLimit)) and prevPoint[0] != 0.0:
                    x1 = dates.date2num(prevPoint[0])
                    y1 = float(prevPoint[1])
                    x2 = dates.date2num(plottingData['cgmValuesX'][i][j])
                    y2 = float(plottingData['cgmValuesY'][i][j])

                    slope, intercept = linregressScipy([x1, x2], [y1, y2])

                    cgmRedLowValuesX[cgmRedLowIndex].append(dates.num2date(np.linalg.solve([[slope]], [cgmRedLowLimit - intercept])[0]))
                    cgmRedLowValuesY[cgmRedLowIndex].append(cgmRedLowLimit)
                if float(plottingData['cgmValuesY'][i][j]) <= cgmRedLowLimit:
                    cgmRedLowValuesX[cgmRedLowIndex].append(plottingData['cgmValuesX'][i][j])
                    cgmRedLowValuesY[cgmRedLowIndex].append(plottingData['cgmValuesY'][i][j])
                if float(plottingData['cgmValuesY'][i][j]) >= cgmRedLowLimit and float(prevPoint[1]) < cgmRedLowLimit:
                    cgmRedLowValuesX.append([])
                    cgmRedLowValuesY.append([])
                    cgmRedLowIndex += 1

                prevPoint = (plottingData['cgmValuesX'][i][j], plottingData['cgmValuesY'][i][j])

            cgmRedHighValuesX.append([])
            cgmRedHighValuesY.append([])
            cgmRedHighIndex += 1
            cgmRedLowValuesX.append([])
            cgmRedLowValuesY.append([])
            cgmRedLowIndex += 1

        for i in range(0, len(cgmRedHighValuesX)):
            cgmRedHighPlot, = axCgmBg.plot(cgmRedHighValuesX[i], cgmRedHighValuesY[i], marker=config["plotMarkers"].get("cgmMarker"),
                                           markersize=config["plotMarkers"].get("cgmMainMarkerSize"),
                                           linewidth=config["linewidths"].getfloat("cgmLineWidth"), color=config["colors"].get("overMaxColor"))  # Plot cgmValues
        for i in range(0, len(cgmRedLowValuesX)):
            cgmRedLowPlot, = axCgmBg.plot(cgmRedLowValuesX[i], cgmRedLowValuesY[i], marker=config["plotMarkers"].get("cgmMarker"),
                                           markersize=config["plotMarkers"].get("cgmMainMarkerSize"),
                                           linewidth=config["linewidths"].getfloat("cgmLineWidth"), color=config["colors"].get("overMaxColor"))  # Plot cgmValues

        cgmAlertPlot, = axCgmBg.plot(plottingData['cgmAlertValuesX'], plottingData['cgmAlertValuesY'], 'ro', color=config["colors"].get("cgmPlotColor"),
                                     mec='#000000',
                                     mew=.5)  # Plot cgmAlertValues (datapoints)
        for i in range(0,len(plottingData['mlCgmValuesX'])):
            mlCgmPlot, = axCgmBg.plot(plottingData['mlCgmValuesX'][i], plottingData['mlCgmValuesY'][i], marker=config["plotMarkers"].get("cgmMarker"), markersize=config["plotMarkers"].get("cgmAdditionalMarkerSize"),
                                    linewidth=config["linewidths"].getfloat("mlCgmLineWidth"), color=config["colors"].get("cgmPlotColor"))

        cgmCalibrationPlot, = axCgmBg.plot(plottingData['cgmCalibrationValuesX'], plottingData['cgmCalibrationValuesY'], config["plotMarkers"].get("cgmCalibrationMarker"), fillstyle='none',
                                     mec=config["colors"].get("cgmCalibrationPlotColor"),
                                     mew=2)  # Plot cgmCalibrationValues (datapoints)

        axCgmBg.plot(plottingData['cgmAlertValuesRedX'], plottingData['cgmAlertValuesRedY'], 'ro', color=config["colors"].get("overMaxColor"), mec='#000000', mew=.5)  # Plot cgmAlertValues (datapoints)

    if config["plotBooleans"].getboolean("plotBg"):
        bgPlot, = axCgmBg.plot(plottingData['bgValuesX'], plottingData['bgValuesY'], linewidth=config["linewidths"].getfloat("bgLineWidth"),
                               color=config["colors"].get("bgPlotColor"))  # Plot bgValues
        axCgmBg.plot(plottingData['bgValuesX'], plottingData['bgValuesY'], 'ro', color=config["colors"].get("bgPlotColor"), mec='#000000',
                     mew=.5)  # Plot bgValues (datapoints)

    axSleep.yaxis.set_minor_locator(MultipleLocator(25))
    axSleep.yaxis.set_major_locator(MultipleLocator(50))

    if config["plotBooleans"].getboolean("plotBolusCalculation"):
        # bolus calculation generics
        for cell in plottingData['bolusCalculationGenerics']:
            bolusCalculationGenericsPlot, = axCgmBg.plot(plottingData['bolusCalculationGenerics'][cell]['X'],
                                                         plottingData['bolusCalculationGenerics'][cell]['Y'],
                                                 marker=config["plotMarkers"].get("bolusCalculationMarker"),
                                                 markersize=config["plotMarkers"].get("bolusCalculationMarkerSize"),
                                                 fillstyle='none', color=plottingData['bolusCalculationGenerics'][cell]['color'],
                                                 linewidth=0, mec=plottingData['bolusCalculationGenerics'][cell]['color'],
                                                 mew=2)  # Plot bgValues (datapoints)
        bolusCalculationPlot, = axCgmBg.plot(plottingData['bolusCalculationValuesX'],
                                             plottingData['bolusCalculationValuesY'],
                                             marker=config["plotMarkers"].get("bolusCalculationMarker"),
                                             markersize=config["plotMarkers"].get("bolusCalculationMarkerSize"),
                                             fillstyle='none', color=config["colors"].get("bolusCalculationColor"),
                                             linewidth=0, mec=config["colors"].get("bolusCalculationColor"),
                                             mew=2)  # Plot bgValues (datapoints)
    # heartRate
    if config["plotBooleans"].getboolean("plotHeartRate") and not config["plotBooleans"].getboolean("plotCgmRaw"):
        axHeartRate = axSleep.twinx()
        axHeartRate.set_ylabel(config["axisLabels"].get("hrLabel"))

        gridSteps = (10.0 + gridOffset)
        hrStepWidth = round(round((((config["limits"].getfloat("maxHrValue") - config["limits"].getfloat("minHrValue")) / gridSteps) + 0.49) / 5) + 0.49) * 5

        minHr = (config["limits"].getfloat("minHrValue") / 10) * 10
        maxHr = minHr + gridSteps * hrStepWidth

        hrTicks = []

        axHeartRate.set_ylim(minHr, maxHr)

        for i in range(0, 11 + gridOffset):
            if i % 2 == 1:
                hrTicks.append(config["limits"].getfloat("minHrValue") + hrStepWidth * i)
        axHeartRate.set_yticks(hrTicks)

        for i in range(0, len(plottingData['heartRateValuesX'])):
            heartRatePlot, = axHeartRate.plot(plottingData['heartRateValuesX'][i], plottingData['heartRateValuesY'][i], marker=config["plotMarkers"].get("heartRateMarker"),
                                          markersize=config["plotMarkers"].get("heartRateMarkerSize"),
                                          linewidth=config["linewidths"].getfloat("heartRateLineWidth"), color=config["colors"].get("heartRatePlotColor"))
        axHeartRate.set_zorder(axSleep.get_zorder() + 1)
    elif config["plotBooleans"].getboolean("plotCgmRaw") and config["plotBooleans"].getboolean("plotCgm"):
        axHeartRate = axSleep.twinx()
        axHeartRate.set_ylabel(config["axisLabels"].get("cgmRawLabel"))
        axHeartRate.set_ylim(5, 75)
        for i in range(0, len(plottingData['cgmRawValuesX'])):
            heartRatePlot, = axHeartRate.plot(plottingData['cgmRawValuesX'][i], plottingData['cgmRawValuesY'][i], linewidth=config["linewidths"].getfloat("cgmRawLineWidth"), color=config["colors"].get("cgmRawPlotColor"))
        axHeartRate.set_zorder(axSleep.get_zorder() + 1)

    # some additional settings
    axSleep.patch.set_visible(False)

    axSleep.spines['bottom'].set_visible(True)
    axSleep.spines['top'].set_visible(False)

    if config["plotBooleans"].getboolean("plotElevation"):
        for i in range(0,len(elevationSlices)):
            if elevationSlicesMax[i] and elevationSlicesMaxCGM[i]:
                # arrow style
                arrowStyle = "-|>"
                if float(elevationSlicesMax[i][0]) > 0:
                    arrowStyle = "<|-"
                # y axis index
                #y1 = elevationSlicesMaxCGM[i]
                y1 = elevationSlicesMax[i][1] - 15
                y2 = y1 + 30

                if abs(elevationSlicesMax[i][0]) > config["limits"].getfloat("glucoseElevationN1"):
                    axCgmBg.annotate("", xy=(elevationSlicesMax[i][2] - datetime.timedelta(minutes=13), y1),
                                     xytext=(elevationSlicesMax[i][2] - datetime.timedelta(minutes=13), y2),
                                     arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
                if abs(elevationSlicesMax[i][0]) > config["limits"].getfloat("glucoseElevationN2"):
                    axCgmBg.annotate("", xy=(elevationSlicesMax[i][2], y1),
                                     xytext=(elevationSlicesMax[i][2], y2),
                                     arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
                if abs(elevationSlicesMax[i][0]) > config["limits"].getfloat("glucoseElevationN3"):
                    axCgmBg.annotate("", xy=(elevationSlicesMax[i][2] + datetime.timedelta(minutes=13), y1),
                                     xytext=(elevationSlicesMax[i][2] + datetime.timedelta(minutes=13), y2),
                                     arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

    # z sort subplots
    axCgmBg.set_zorder(axSleep.get_zorder() + 2)

    ########## CGM, BG, HR PLOT ##########



    ########## Symbols ##########
    if config["plotBooleans"].getboolean("plotSymbols"):
        pumpRewindPlot, = axSymbols.plot(plottingData['pumpRewindX'], plottingData['pumpRewindY'], config["symbolMarkers"].get("rewindMarker"), color=config["colors"].get("pumpColor"))
        pumpKatErrPlot, = axSymbols.plot(plottingData['pumpKatErrX'], plottingData['pumpKatErrY'], config["symbolMarkers"].get("katErrMarker"), color=config["colors"].get("symbolsColor"))
        exercisePlot, = axSymbols.plot(plottingData['MarkerExerciseX'], plottingData['MarkerExerciseY'], config["symbolMarkers"].get("exerciseMarker"), color=config["colors"].get("symbolsColor"))
        cgmCalibrationErrorPlot, = axSymbols.plot(plottingData['MarkerCgmCalibrationErrorX'], plottingData['MarkerCgmCalibrationErrorY'], config["symbolMarkers"].get("cgmCalibrationErrorMarker"), color=config["colors"].get("symbolsColor"))
        cgmConnectionErrorPlot, = axSymbols.plot(plottingData['MarkerCgmConnectionErrorX'], plottingData['MarkerCgmConnectionErrorY'], config["symbolMarkers"].get("cgmConnectionErrorMarker"), color=config["colors"].get("symbolsColor"))
        cgmSensorFinishedPlot, = axSymbols.plot(plottingData['MarkerCgmSensorFinishedX'], plottingData['MarkerCgmSensorFinishedY'], config["symbolMarkers"].get("cgmSensorFinishedMarker"), color=config["colors"].get("symbolsColor"))
        cgmSensorStartPlot, = axSymbols.plot(plottingData['MarkerCgmSensorStartX'], plottingData['MarkerCgmSensorStartY'], config["symbolMarkers"].get("cgmSensorStartMarker"), color=config["colors"].get("symbolsColor"))
        cgmTimeSyncPlot, = axSymbols.plot(plottingData['MarkerCgmTimeSyncX'], plottingData['MarkerCgmTimeSyncY'], config["symbolMarkers"].get("cgmTimeSyncMarker"), color=config["colors"].get("symbolsColor"))
        pumpReservoirEmptyPlot, = axSymbols.plot(plottingData['MarkerPumpReservoirEmptyX'], plottingData['MarkerPumpReservoirEmptyY'], config["symbolMarkers"].get("pumpReservoirEmptyMarker"), color=config["colors"].get("symbolsColor"))

        # cgm generics
        for cell in plottingData['symbolGenerics']:
            symbolGenericPlot, = axSymbols.plot(plottingData['symbolGenerics'][cell]['X'],
                                                     plottingData['symbolGenerics'][cell]['Y'],
                                                     plottingData['symbolGenerics'][cell]['marker'],
                                                     color=plottingData['symbolGenerics'][cell]['color'])

        axSymbols.spines['bottom'].set_visible(True)
        axSymbols.spines['top'].set_visible(False)

        # background field for the symbols
        axSymbols.axhspan(0.9, 1.1, facecolor=config["colors"].get("symbolsBackgroundColor"), alpha=1, edgecolor='#000000')

        axSymbols.set_ylim([0.9, 1.1])
    ########## Symbols ##########


    ########## Exercise Time Value ##########
    if config["plotBooleans"].getboolean("plotExercise"):
        for i in range(0,len(plottingData['exerciseX'])):
            tempWidth = dates.date2num(plottingData['exerciseX'][i] + datetime.timedelta(minutes=float(plottingData['exerciseY'][i]))) - dates.date2num(plottingData['exerciseX'][i])
            axExercise.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['exerciseX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    1,  # height
                    facecolor=plottingData['exerciseColor'][i],
                    alpha=1.0
                )
            )
    ########## Exercise Time Value ##########

    ########## Stress Level ##########
    if config["plotBooleans"].getboolean("plotStress"):
        plottingData['stressX'].append(endDate)
        for i in range(0,len(plottingData['stressX'])-1):
            tempWidth = dates.date2num(plottingData['stressX'][i + 1]) - dates.date2num(plottingData['stressX'][i])
            if float(plottingData['stressY'][i]) == 0.0:
                tempStressColor = "#FFFFFF"
            elif float(plottingData['stressY'][i]) <= 25.0:
                tempStressColor = config["colors"].get("stress1Color")
            elif float(plottingData['stressY'][i]) <= 50.0:
                tempStressColor = config["colors"].get("stress2Color")
            elif float(plottingData['stressY'][i]) <= 75.0:
                tempStressColor = config["colors"].get("stress3Color")
            else:
                tempStressColor = config["colors"].get("stress4Color")

            # pseudo white rect to eliminte potential overlapping
            axStress.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['stressX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    1,  # height
                    facecolor=tempStressColor,
                    alpha=1.0
                )
            )
    ########## Stress Level ##########


    ########## Location ##########
    if config["plotBooleans"].getboolean("plotLocation"):
        plottingData['locationX'].append(endDate)
        for i in range(0,len(plottingData['locationX'])-1):
            tempString = ""
            tempColor = ""
            if plottingData['locationY'][i] == "LOC_TRANSITION":
                tempString = config["locations"].get("locTransitionLabel")
                tempColor = config["locations"].get("locTransitionColor")
            if plottingData['locationY'][i] == "LOC_HOME":
                tempString = config["locations"].get("locHomeLabel")
                tempColor = config["locations"].get("locHomeColor")
            if plottingData['locationY'][i] == "LOC_WORK":
                tempString = config["locations"].get("locWorkLabel")
                tempColor = config["locations"].get("locWorkColor")
            if plottingData['locationY'][i] == "LOC_FOOD":
                tempString = config["locations"].get("locFoodLabel")
                tempColor = config["locations"].get("locFoodColor")
            if plottingData['locationY'][i] == "LOC_SPORTS":
                tempString = config["locations"].get("locSportsLabel")
                tempColor = config["locations"].get("locSportsColor")
            if plottingData['locationY'][i] == "LOC_OTHER":
                tempString = config["locations"].get("locOtherLabel")
                tempColor = config["locations"].get("locOtherColor")

            tempWidth = dates.date2num(plottingData['locationX'][i + 1]) - dates.date2num(plottingData['locationX'][i])
            t = axLocation.text(dates.date2num(plottingData['locationX'][i])+ tempWidth/2, 0.4, tempString, style='italic', color='#000000', alpha=0.5, horizontalalignment='center', verticalalignment='center')
            l = axLocation.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['locationX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    1,  # height
                    #edgecolor="#000000",
                    facecolor=tempColor
                )
            )
            if (t.get_window_extent(renderer=fig.canvas.get_renderer()).width - 16) > l.get_window_extent(renderer=fig.canvas.get_renderer()).width:
                axLocation.texts.remove(t)
    ########## Location ##########


    ########## Legend ##########
    ## offset calc for legend ##
    axbox = axBarPlots.get_position()
    handles = []
    labels = []
    xOffset = -0.05
    yOffset = 0.29

    if not extLegend:
        if config["plotBooleans"].getboolean("plotBg"):
            handles.append(bgPlot)
            labels.append(config["legendLabels"].get("bgLegend"))
        if config["plotBooleans"].getboolean("plotCgm"):
            handles.append(cgmPlot)
            labels.append(config["legendLabels"].get("cgmLegend"))
        xOffset = config["limits"].getfloat("legendXOffset")
        yOffset = config["limits"].getfloat("legendYOffset")
    else:
        handles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("autonomousSuspendColor"),
                alpha=0.5
            ))
        labels.append(config["legendLabels"].get("autonomousSuspendLegend"))
    if config["plotBooleans"].getboolean("plotBasal"):
        handles.append(basalPlot)
        labels.append(config["legendLabels"].get("basalLegend"))
    if (config["plotBooleans"].getboolean("plotBolus") and len(bolusBars) > 0):
        handles.append(bolusBars)
        labels.append(config["legendLabels"].get("bolusLegend"))
    if (config["plotBooleans"].getboolean("plotCarb") and len(carbBars) > 0):
        handles.append(carbBars)
        labels.append(config["legendLabels"].get("carbLegend"))

    if len(handles) > 0 and plotType == "SLICE_DAILY":
        if (plotCounter[plotType] % 3) == 0 or extLegend:
            dailyLegend = fig.legend(handles, labels, loc="upper left",
                                     bbox_to_anchor=[axbox.x0 + xOffset, axbox.y0 + yOffset], fontsize=10,
                                     edgecolor='#000000')
            dailyLegend.get_frame().set_alpha(0.6)


    ### extended legend ###
    if extLegend:
        axbox2 = axSleep.get_position()
        plotsLegendHandles = []
        plotsLegendLabels = []

        if config["plotBooleans"].getboolean("plotBg"):
            plotsLegendHandles.append(bgPlot)
            plotsLegendLabels.append(config["legendLabels"].get("bgLegend"))
        if config["plotBooleans"].getboolean("plotCgm"):
            plotsLegendHandles.append(cgmPlot)
            plotsLegendLabels.append(config["legendLabels"].get("cgmLegend"))
            plotsLegendHandles.append(cgmAlertPlot)
            plotsLegendLabels.append(config["legendLabels"].get("cgmAlertLegend"))
            plotsLegendHandles.append(cgmCalibrationPlot)
            plotsLegendLabels.append(config["legendLabels"].get("cgmCalibrationLegend"))
            plotsLegendHandles.append(mlCgmPlot)
            plotsLegendLabels.append(config["legendLabels"].get("mlCgmLegend"))
            plotsLegendHandles.append(pumpCgmPredictionPlot)
            plotsLegendLabels.append(config["legendLabels"].get("pumpCgmPredictionLegend"))
        if config["plotBooleans"].getboolean("plotBolusCalculation"):
            plotsLegendHandles.append(bolusCalculationPlot)
            plotsLegendLabels.append(config["legendLabels"].get("bolusCalculatonLegend"))
        if config["plotBooleans"].getboolean("plotHeartRate") and not config["plotBooleans"].getboolean("plotCgmRaw"):
            plotsLegendHandles.append(heartRatePlot)
            plotsLegendLabels.append(config["legendLabels"].get("heartRateLegend"))

        if config["plotBooleans"].getboolean("plotSleep"):
            plotsLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("deepSleepColor"),
                alpha=1.0
            ))
            plotsLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("lightSleepColor"),
                alpha=1.0
            ))
            plotsLegendLabels.append(config["sleepLabel"].get("deepSleepLabel"))
            plotsLegendLabels.append(config["sleepLabel"].get("lightSleepLabel"))

        if len(plotsLegendHandles) > 0:
            axbox2 = axSleep.get_position()
            plotsLegend = fig.legend(plotsLegendHandles, plotsLegendLabels, loc="lower left",
                       bbox_to_anchor=[axbox2.x0 + config["limits"].getfloat("legendXOffset"), axbox2.y0], fontsize=10,
                       edgecolor='#000000')
            plotsLegend.get_frame().set_alpha(0.9)


        if config["plotBooleans"].getboolean("plotSymbols"):
            rewindPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("rewindMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            katErrPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("katErrMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            exercisePlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("exerciseMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            cgmCalibrationErrorPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmCalibrationErrorMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            cgmConnectionErrorPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmConnectionErrorMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            cgmSensorFinishedPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmSensorFinishedMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            cgmSensorStartPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmSensorStartMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            cgmTimeSyncPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmTimeSyncMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
            pumpReservoirEmptyPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("pumpReservoirEmptyMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))

            symbolsLegendHandles = [rewindPlot, katErrPlot, exercisePlot, cgmCalibrationErrorPlot, cgmConnectionErrorPlot, cgmSensorFinishedPlot, cgmSensorStartPlot, cgmTimeSyncPlot, pumpReservoirEmptyPlot]
            symbolsLegendLabels = [config["symbolLabels"].get("pumpRewindLegend"), config["symbolLabels"].get("pumpKatErrLegend").replace(' ', '\n'), config["symbolLabels"].get("exerciseLegend"), config["symbolLabels"].get("cgmCalibrationErrorLegend"), config["symbolLabels"].get("cgmConnectionErrorLegend"),
                                   config["symbolLabels"].get("cgmSensorFinishedLegend"), config["symbolLabels"].get("cgmSensorStartLegend"), config["symbolLabels"].get("cgmTimeSyncLegend"), config["symbolLabels"].get("pumpReservoirEmptyLegend")]

            symbolsLegend = fig.legend(symbolsLegendHandles, symbolsLegendLabels, loc="upper left",
                       bbox_to_anchor=[axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.675, axbox2.y0 + 0.55], fontsize=10,
                       edgecolor='#000000')
            symbolsLegend.get_frame().set_alpha(0.9)

            axSymbols.annotate('', xy=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.67, 0.4), xycoords='axes fraction',
                               xytext=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.71, -1.7),
                               arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axSymbols.text(dates.date2num(beginDate) + 0.01, 1, "Symbols", color='#000000',
                           horizontalalignment='left', verticalalignment='center')


        if config["plotBooleans"].getboolean("plotLocation"):
            locationLegendHandles = []
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locNoDataColor"),
                edgecolor="#000000",
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locTransitionColor"),
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locHomeColor"),
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locWorkColor"),
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locFoodColor"),
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locSportsColor"),
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["locations"].get("locOtherColor"),
                alpha=1.0
            ))

            locationLegendLabels = [config["locations"].get("locNoDataLabel"), config["locations"].get("locTransitionLabel"), config["locations"].get("locHomeLabel"), config["locations"].get("locWorkLabel"), config["locations"].get("locFoodLabel"), config["locations"].get("locSportsLabel"), config["locations"].get("locOtherLabel")]

            axbox2 = axSleep.get_position()
            locationLegend = fig.legend(locationLegendHandles, locationLegendLabels, loc="upper left",
                       bbox_to_anchor=[axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.23, axbox2.y0 + 0.55], fontsize=10,
                       edgecolor='#000000')
            locationLegend.get_frame().set_alpha(0.9)

            axLocation.annotate('', xy=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.1525, 0.5), xycoords='axes fraction',
                               xytext=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.1925, -1.6),
                               arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axLocation.text(dates.date2num(beginDate) + 0.01, 0.4, "Location", color='#000000',
                            horizontalalignment='left', verticalalignment='center')

        if config["plotBooleans"].getboolean("plotExercise"):
            exerciseLegendHandles = []
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor='#ffffff',
                edgecolor="#000000",
                alpha=1.0
            ))
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("exerciseLowColor"),
                alpha=1.0
            ))
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("exerciseMidColor"),
                alpha=1.0
            ))
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("exerciseHighColor"),
                alpha=1.0
            ))

            exerciseLegendLabels = ["no data", config["exerciseLabel"].get("exerciseLowLabel"), config["exerciseLabel"].get("exerciseMidLabel"), config["exerciseLabel"].get("exerciseHighLabel")]

            axbox2 = axSleep.get_position()
            exerciseLegend = fig.legend(exerciseLegendHandles, exerciseLegendLabels, loc="upper left",
                                        bbox_to_anchor=[axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.38, axbox2.y0 + 0.49],
                                        fontsize=10,
                                        edgecolor='#000000')
            exerciseLegend.get_frame().set_alpha(0.9)

            axExercise.annotate('', xy=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.327, 0.5), xycoords='axes fraction',
                               xytext=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.367, -1.6),
                               arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axExercise.text(dates.date2num(beginDate) + 0.01, 0.4, "Exercise", color='#000000',
                            horizontalalignment='left', verticalalignment='center')

        if config["plotBooleans"].getboolean("plotStress"):
            stressLegendHandles = []
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("stress0Color"),
                edgecolor="#000000",
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("stress1Color"),
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("stress2Color"),
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("stress3Color"),
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config["colors"].get("stress4Color"),
                alpha=1.0
            ))

            stressLegendLabels = [config["stressLabels"].get("stress0Label"), config["stressLabels"].get("stress1Label"), config["stressLabels"].get("stress2Label"), config["stressLabels"].get("stress3Label"), config["stressLabels"].get("stress4Label")] # no label for nor data (stress = 0)

            stressLegend = fig.legend(stressLegendHandles, stressLegendLabels, loc="upper left",
                                      bbox_to_anchor=[axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.55, axbox2.y0 + 0.46], fontsize=10,
                                      edgecolor='#000000')
            stressLegend.get_frame().set_alpha(0.9)

            axStress.annotate('', xy=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.525, 0.5), xycoords='axes fraction',
                           xytext=(axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.565, -1.6),
                           arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axStress.text(dates.date2num(beginDate) + 0.01, 0.4, "Stress", color='#000000',
                        horizontalalignment='left', verticalalignment='center')

            ## elevation legend ##
            placeholder = patches.Rectangle((0, 0), 1, 1, fill=False, edgecolor='none', visible=False)
            elevationLegend = axCgmBg.legend([placeholder, placeholder, placeholder, placeholder, placeholder, placeholder],
                                 [config["legendLabels"].get("lowElevationUpLegend"), config["legendLabels"].get("midElevationUpLegend"), config["legendLabels"].get("highElevationUpLegend"), config["legendLabels"].get("lowElevationDownLegend"), config["legendLabels"].get("midElevationDownLegend"), config["legendLabels"].get("highElevationDownLegend")],
                                 loc="upper left",
                                 bbox_to_anchor=[axbox2.x0 + config["limits"].getfloat("legendXOffset") + 0.42, axbox2.y0 + 0.126],
                                 fontsize=10, edgecolor='#000000')
            elevationLegend.set_zorder(2)

            elevationLegendTimeOffset = 735
            elevationLegendCGMOffset = 116
            arrowStyle = "<|-"
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 40),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                     elevationLegendCGMOffset + 60),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 20),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                     elevationLegendCGMOffset + 40),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset + 20),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                     elevationLegendCGMOffset + 40),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                     elevationLegendCGMOffset + 20),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                     elevationLegendCGMOffset + 20),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26), elevationLegendCGMOffset),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26),
                                     elevationLegendCGMOffset + 20),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

        elevationLegendCGMOffset -= 60
        arrowStyle = "-|>"
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 40),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                 elevationLegendCGMOffset + 60),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 20),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                 elevationLegendCGMOffset + 40),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset + 20),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                 elevationLegendCGMOffset + 40),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                 elevationLegendCGMOffset + 20),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                 elevationLegendCGMOffset + 20),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26), elevationLegendCGMOffset),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26),
                                 elevationLegendCGMOffset + 20),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config["colors"].get("glucoseElevationColor")))

    ########## Legend ##########


    ########## Generate filename and save fig ##########
    tempDaycounterStr = beginDate.strftime(config["fileSettings"].get("filenameDateFormatString")) + plotType

    if tempDaycounterStr in dayCounter:
        dayCounter[tempDaycounterStr] += 1
    else:
        dayCounter[tempDaycounterStr] = 0

    # filename temp vars
    tempPrefix = config["fileSettings"].get("filenamePrefix")
    tempTimestamp = beginDate.strftime(config["fileSettings"].get("filenameDateFormatString"))
    tempDaycounter = str(dayCounter[tempDaycounterStr])
    tempFileext = config["fileSettings"].get("fileExtension")
    filename = "unnamed.pdf"

    if extLegend:
        filename = config["fileSettings"].get("legendFileDetailed")
    elif plotType == "SLICE_DAILYSTATISTICS":
        filename = tempPrefix + "dailystatistics_" + tempTimestamp + tempFileext
    elif plotType == "SLICE_DAILY":
        filename = tempPrefix + "daily_" + tempTimestamp + tempFileext
    elif plotType == "SLICE_TINY":
        filename = tempPrefix + "tinyslice_" + tempTimestamp + "_" + tempDaycounter + tempFileext
    elif plotType == "SLICE_NORMAL":
        filename = tempPrefix + "normalslice_" + tempTimestamp + "_" + tempDaycounter + tempFileext
    elif plotType == "SLICE_BIG":
        filename = tempPrefix + "bigslice_" + tempTimestamp + "_" + tempDaycounter + tempFileext

    ### canvas fixes
    axBarPlots.set_xlim(beginDate, endDate)
    fig.tight_layout()
    # hide 00:00
    axBarPlots.xaxis.get_major_ticks()[0].label1.set_visible(False)
    # remove overlapping yaxis ticks
    axBarPlots.yaxis.get_major_ticks()[-2].label1.set_visible(False)
    if config["plotBooleans"].getboolean("plotBasal"):
        axBasal.yaxis.get_major_ticks()[-2].label2.set_visible(False)


    sliceWidth = 1.0
    ### plotType specific settings
    if plotType == "SLICE_DAILY" or plotType == "SLICE_DAILYSTATISTICS":
        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.93, left=0.07)
        axSleep.get_yaxis().set_label_coords(-0.06, 0.5)
        if axHeartRate:
            axHeartRate.get_yaxis().set_label_coords(1.06, 0.5)
        axBarPlots.get_yaxis().set_label_coords(-0.06, 0.5)
        axBasal.get_yaxis().set_label_coords(1.06, 0.5)
    elif plotType == "SLICE_TINY":
        axSleep.get_yaxis().set_label_coords(-0.16, 0.5)
        if axHeartRate:
            axHeartRate.get_yaxis().set_label_coords(1.16, 0.5)
        axBarPlots.get_yaxis().set_label_coords(-0.16, 0.5)
        axBasal.get_yaxis().set_label_coords(1.16, 0.5)

        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.85, left=0.15)
        sliceWidth = 0.395
    elif plotType == "SLICE_NORMAL":
        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.90, left=0.10)
        sliceWidth = 0.54
    elif plotType == "SLICE_BIG":
        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.93, left=0.07)
        axSleep.get_yaxis().set_label_coords(-0.06, 0.5)
        if axHeartRate:
            axHeartRate.get_yaxis().set_label_coords(1.06, 0.5)
        axBarPlots.get_yaxis().set_label_coords(-0.06, 0.5)
        axBasal.get_yaxis().set_label_coords(1.06, 0.5)
    plotCounter[plotType] += 1

    scale = 1.5
    tempDailyNotes = ""
    if dailyNotes:
        fig.set_size_inches(6.1 * scale * sliceWidth, 11.69 / 3 * scale)
        tempDailyNotes = generateDailyNotes(config, outPath, plottingData, beginDate)
    else:
        fig.set_size_inches(8.27 * scale * sliceWidth, 11.69 / 3 * scale)

    plt.savefig(os.path.join(outPath, filename))
    plt.clf()
    ########## Generate filename and save fig ##########

    return {'filename':filename, 'dailyNotes':tempDailyNotes}

def generatePlotListTex(config, outPath, slicesInRow, plots, plotListFileName, headerFileName):
    texPlotListSlices = open(os.path.join(outPath, plotListFileName), 'w')
    slicesTableLabels = ""
    slicesTablePlots = ""

    for i in range(0, len(plots)):
        if i % (3*slicesInRow) == 0:
            texPlotListSlices.write("\\newpage\n\\input{" + os.path.splitext(headerFileName)[0]+ "}\n")

        slicesTableLabels += plots[i]['timestamp'].strftime(config["axisLabels"].get("titelDateFormat")) + " &"
        if slicesInRow == 1:
            trim = "0 0 0 0"
        elif slicesInRow == 2:
            if i % slicesInRow == 0:
                trim = "0 0 1.36cm 0"
            elif i % slicesInRow == 1:
                trim = "1.36cm 0 0 0"
        elif slicesInRow == 3:
            if i % slicesInRow == 0:
                trim = "0 0 1.73cm 0"
            elif i % slicesInRow == 1:
                trim = "1.73cm 0 1.73cm 0"
            elif i % slicesInRow == 2:
                trim = "1.73cm 0 0 0"

        slicesTablePlots += "\\includegraphics[scale=0.53,keepaspectratio,trim={" + trim + "},clip]{" + plots[i]['filename'] + "} &"

        if (i % slicesInRow == (slicesInRow - 1)) or i == (len(plots) - 1):
            additionalCols = ""
            if i == (len(plots) - 1):
                for i in range(0,slicesInRow - (i % slicesInRow) - 1):
                    additionalCols += " &"
            texPlotListSlices.write("\\begin{tabularx}{\linewidth}{YYY}\n" + slicesTableLabels[:-2] + additionalCols + "\\\\\n" + slicesTablePlots[:-2] + additionalCols + "\n\\end{tabularx}\n\n")
            slicesTableLabels = ""
            slicesTablePlots = ""

    texPlotListSlices.close()

def generateDailyPlotListWithNotesTex(config, outpath, plots, dailyNotes):
    texPlotListSlices = open(os.path.join(outpath, config["fileSettings"].get("plotListFileDailyStatistics")), 'w')
    slicesTableLabels = ""
    slicesTablePlots = ""

    for i in range(0, len(plots)):
        if i % 3 == 0:
            texPlotListSlices.write("\\newpage\n\\input{" + os.path.splitext(config["fileSettings"].get("headerFileDailyStatistics"))[0]+ "}\n")

        slicesTableLabels += plots[i]['timestamp'].strftime(config["axisLabels"].get("titelDateFormat")) + " &"
        trim = "0 0 0 0"

        if (dailyNotes[i]['basalTotal'] + dailyNotes[i]['bolusTotal']) > 0.0:
            basalTotal = float(
                dailyNotes[i]['basalTotal'] / (dailyNotes[i]['basalTotal'] + dailyNotes[i]['bolusTotal']) * 100)
            bolusTotal = float(
                dailyNotes[i]['bolusTotal'] / (dailyNotes[i]['basalTotal'] + dailyNotes[i]['bolusTotal']) * 100)
        else:
            basalTotal = 0.0
            bolusTotal = 0.0

        slicesTablePlots += "\\includegraphics[scale=0.53,keepaspectratio,trim={" + trim + "},clip,valign=t]{" + plots[i]['filename'] + "} &\n"
        slicesTablePlots += "\\begin{tabular}[t]{ccc}\n"
        slicesTablePlots += "\\includegraphics[scale=0.09,keepaspectratio,valign=t]{" + dailyNotes[i]['timeInRangeFilename'] + "} & \\includegraphics[scale=0.09,keepaspectratio,valign=t]{" + dailyNotes[i]['hypoFilename'] + "} & \\includegraphics[scale=0.09,keepaspectratio,valign=t]{" + dailyNotes[i]['hyperFilename'] + "} \\\\\n"
        slicesTablePlots += config["dailyStatistics"].get("labelTimeInRange") + " & " + config["dailyStatistics"].get("labelHypo") + " & " + config["dailyStatistics"].get("labelHyper") + "\\\\\n"
        slicesTablePlots += "\\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config["dailyStatistics"].get("labelBasalTotal") + ": " + str('{0:.2f}'.format(dailyNotes[i]['basalTotal'])) + " IE (" + str('{0:.2f}'.format(basalTotal)) + "\\%)} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config["dailyStatistics"].get("labelBolusTotal") + ": " + str('{0:.2f}'.format(dailyNotes[i]['bolusTotal'])) + " IE (" + str('{0:.2f}'.format(bolusTotal)) + "\\%)} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config["dailyStatistics"].get("labelCarbTotal") + ": " + str('{0:.2f}'.format(dailyNotes[i]['carbTotal'])) + " KE} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config["dailyStatistics"].get("labelAutonomousSuspentionTime") + ": " + str(dailyNotes[i]['autonomousSuspensionTotal']) + "h} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config["dailyStatistics"].get("labelNote") + ": " + dailyNotes[i]['noteAnnotation'] + "}\n"
        slicesTablePlots += "\\end{tabular}  "

        additionalCols = ""
        texPlotListSlices.write("\\begin{tabularx}{\linewidth}{ccc}\n" + slicesTableLabels[:-2] + additionalCols + "\\\\\n" + slicesTablePlots[:-2] + additionalCols + "\n\\end{tabularx}\n\n")
        slicesTableLabels = ""
        slicesTablePlots = ""

    texPlotListSlices.close()

def generateDailyPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 1, plots, config["fileSettings"].get("plotListFileDaily"), config["fileSettings"].get("headerFileDaily"))

def generateTinySlicesPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 3, plots, config["fileSettings"].get("plotListFileTinySlices"), config["fileSettings"].get("headerFileTinySlices"))

def generateNormalSlicesPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 2, plots, config["fileSettings"].get("plotListFileNormalSlices"), config["fileSettings"].get("headerFileNormalSlices"))

def generateBigSlicesPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 1, plots, config["fileSettings"].get("plotListFileBigSlices"), config["fileSettings"].get("headerFileBigSlices"))

def generateDailyNotes(config, outPath, plottingData, beginDate):
    timeInRangeFilename = "timeInRange_" + beginDate.strftime(config["fileSettings"].get("filenameDateFormatString")) + ".png"
    hyperFilename = "hyper_" + beginDate.strftime(config["fileSettings"].get("filenameDateFormatString")) + ".png"
    hypoFilename = "hypo_" + beginDate.strftime(config["fileSettings"].get("filenameDateFormatString")) + ".png"

    fig1 = plt.figure(2)
    ax1 = fig1.add_subplot(111)
    ax1.pie([float(plottingData['cgmValuesInRange']),
             (plottingData['cgmValuesTotal'] - plottingData['cgmValuesInRange'])], explode=(0.0, 0),
            shadow=False, colors=(config["dailyStatistics"].get("colorTimeInRange"), config["dailyStatistics"].get("colorNone")), startangle=90)
    ax1.axis('equal')  # Equal aspect ratio ensures that pie is drawn as a circle.
    ax1.text(0, 0, str('{0:.2f}'.format(float(plottingData['cgmValuesInRange']) / float(plottingData['cgmValuesTotal']) * 100)) + "%",
        fontsize=80, horizontalalignment='center', verticalalignment='center')

    plt.subplots_adjust(left=0.0, right=1.0, top=1.04, bottom=-0.04)
    plt.savefig(os.path.join(outPath, timeInRangeFilename))

    ax1.clear()
    ax1.pie([float(plottingData['cgmValuesAbove']),
             (plottingData['cgmValuesTotal'] - float(plottingData['cgmValuesAbove']))], explode=(0.0, 0),
            shadow=False, colors=(config["dailyStatistics"].get("colorHyper"), config["dailyStatistics"].get("colorNone")), startangle=90 + (
        float(plottingData['cgmValuesTotal'] - plottingData['cgmValuesBelow'] - plottingData['cgmValuesAbove']) / float(
            plottingData['cgmValuesTotal']) * 360.0))
    ax1.axis('equal')
    ax1.text(0, 0, str('{0:.2f}'.format(float(plottingData['cgmValuesAbove']) / float(plottingData['cgmValuesTotal']) * 100)) + "%",
        fontsize=80, horizontalalignment='center', verticalalignment='center')

    plt.subplots_adjust(left=0.0, right=1.0, top=1.04, bottom=-0.04)
    plt.savefig(os.path.join(outPath, hyperFilename))

    ax1.clear()
    ax1.pie([float(plottingData['cgmValuesBelow']),
             (plottingData['cgmValuesTotal'] - float(plottingData['cgmValuesBelow']))], explode=(0.0, 0),
            shadow=False, colors=(config["dailyStatistics"].get("colorHypo"), config["dailyStatistics"].get("colorNone")), startangle=90 + (
        float(plottingData['cgmValuesTotal'] - plottingData['cgmValuesBelow'] - plottingData['cgmValuesAbove']) / float(
            plottingData['cgmValuesTotal']) * 360.0) + (float(plottingData['cgmValuesAbove']) / float(
            plottingData['cgmValuesTotal']) * 360.0))
    ax1.axis('equal')
    ax1.text(0, 0, str('{0:.2f}'.format(float(plottingData['cgmValuesBelow']) / float(plottingData['cgmValuesTotal']) * 100)) + "%",
        fontsize=80, horizontalalignment='center', verticalalignment='center')

    plt.subplots_adjust(left=0.0, right=1.0, top=1.04, bottom=-0.04)
    plt.savefig(os.path.join(outPath, hypoFilename))

    plt.figure(1)
    plt.close(2)

    return {'cgmValuesTotal':float(plottingData['cgmValuesTotal']),
            'cgmValuesAbove':float(plottingData['cgmValuesAbove']),
            'cgmValuesBelow':float(plottingData['cgmValuesBelow']),
            'bolusTotal': float(plottingData['bolusTotal']),
            'basalTotal': float(plottingData['basalTotal']),
            'carbTotal': float(plottingData['carbTotal']),
            'autonomousSuspensionTotal': plottingData['autonomousSuspensionTotal'],
            'timeInRange': float(plottingData['cgmValuesTotal'] - plottingData['cgmValuesBelow']
                                 - plottingData['cgmValuesAbove']),
            'timeInRangeFilename': timeInRangeFilename, 'hypoFilename': hypoFilename, 'hyperFilename': hyperFilename,
            'noteAnnotation': plottingData['noteAnnotation']}


def generateLegendTex(config, outpath, filename):
    fileLegendTex = open(os.path.join(outpath, "tex_g_legendPage.tex"), 'w')
    if filename:
        fileLegendTex.write("\\newpage\n\\centerline{\\huge\\textbf{Legend}}\n\\vspace{2em}\n\\centering{\\includegraphics[width=\\textwidth]{" + filename + "}}\n\\vspace{1em}\n\\input{tex_s_legendText}")
    fileLegendTex.close()

def generateHeaderTex(config, outpath, filename, headLine):
    headerFile = open(os.path.join(outpath, filename), 'w')
    headerFile.write(
        "\\noindent \\large{\\textbf{" + headLine + "}} \\hfill \\small{Page \\thepage/\\pageref{LastPage}}\n\n\\vspace{0.5em}\n")
    headerFile.write(
        "\centerline{\\includegraphics{" + config["fileSettings"].get("legendFileSymbols") + "}}\n\\vspace{0.1em}")
    headerFile.close()

def generateSymbolsLegend(config, outPath):
    global rewindAvailable
    global katErrAvailable
    global exerciseAvailable
    global cgmCalibrationErrorAvailable
    global cgmConnectionErrorAvailable
    global cgmSensorFinishedAvailable
    global cgmSensorStartAvailable
    global cgmTimeSyncAvailable
    global pumpReservoirEmptyAvailable

    rewindPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("rewindMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    katErrPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("katErrMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    exercisePlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("exerciseMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    cgmCalibrationErrorPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmCalibrationErrorMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    cgmConnectionErrorPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmConnectionErrorMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    cgmSensorFinishedPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmSensorFinishedMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    cgmSensorStartPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmSensorStartMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    cgmTimeSyncPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("cgmTimeSyncMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))
    pumpReservoirEmptyPlot = lines.Line2D([], [], marker=config["symbolMarkers"].get("pumpReservoirEmptyMarker"), linestyle='None', color=config["colors"].get("symbolsColor"))

    handles = []
    labels = []

    if rewindAvailable:
        handles.append(rewindPlot)
        labels.append(config["symbolLabels"].get("pumpRewindLegend"))
    if katErrAvailable:
        handles.append(katErrPlot)
        labels.append(config["symbolLabels"].get("pumpKatErrLegend"))
    if exerciseAvailable:
        handles.append(exercisePlot)
        labels.append(config["symbolLabels"].get("exerciseLegend"))
    if cgmCalibrationErrorAvailable:
        handles.append(cgmCalibrationErrorPlot)
        labels.append(config["symbolLabels"].get("cgmCalibrationErrorLegend"))
    if cgmConnectionErrorAvailable:
        handles.append(cgmConnectionErrorPlot)
        labels.append(config["symbolLabels"].get("cgmConnectionErrorLegend"))
    if cgmSensorFinishedAvailable:
        handles.append(cgmSensorFinishedPlot)
        labels.append(config["symbolLabels"].get("cgmSensorFinishedLegend"))
    if cgmSensorStartAvailable:
        handles.append(cgmSensorStartPlot)
        labels.append(config["symbolLabels"].get("cgmSensorStartLegend"))
    if cgmTimeSyncAvailable:
        handles.append(cgmTimeSyncPlot)
        labels.append(config["symbolLabels"].get("cgmTimeSyncLegend"))
    if pumpReservoirEmptyAvailable:
        handles.append(pumpReservoirEmptyPlot)
        labels.append(config["symbolLabels"].get("pumpReservoirEmptyLegend"))

    figLegend = pylab.figure()
    legend = pylab.figlegend(handles, labels, 'center', numpoints=1, fontsize=6, ncol=4, columnspacing=2)
    figLegend.canvas.draw()

    bbox = legend.get_window_extent().transformed(figLegend.dpi_scale_trans.inverted())
    ll, ur = bbox.get_points()
    x0, y0 = ll
    x1, y1 = ur
    w, h = x1 - x0, y1 - y0
    x1, y1 = x0 + w * 1, y0 + h * 1.1
    bbox = transforms.Bbox(np.array(((x0, y0), (x1, y1))));

    plt.savefig(os.path.join(outPath, config["fileSettings"].get("legendFileSymbols")), bbox_inches=bbox)
    plt.close()

def main():
    versionString = "OpenDiabetesVault-Plot v0.9"
    outPath = os.getcwd()
    csvFileName = ""
    dataset = ""

    parser = OptionParser(usage="%prog [OPTION] [FILE ..]", version=versionString, description=versionString)

    parser.add_option("-f", "--data-set", dest="dataset", metavar="FILE",
                      help="FILE specifies the dataset for the plot.")
    parser.add_option("-c", "--config", dest="config", metavar="FILE", default="config.ini",
                      help="FILE specifies configuration file for the plot [Default config.ini].")
    parser.add_option("-d", "--daily", dest="daily", action="store_true",
                      help="Activates daily plot.")
    parser.add_option("-s", "--dailystatistics", dest="dailystatistics", action="store_true",
                      help="Activates daily plot with daily statistics.")
    parser.add_option("-t", "--slice-tiny", dest="slicetiny", metavar="FILE",
                      help="FILE specifies slice annotations. Activates slice plot (3 per Line).")
    parser.add_option("-n", "--slice-normal", dest="slicenormal", metavar="FILE",
                      help="FILE specifies slice annotations. Activates slice plot (2 per Line).")
    parser.add_option("-b", "--slice-big", dest="slicebig", metavar="FILE",
                      help="FILE specifies slice annotations. Activates slice plot (1 per Line).")
    parser.add_option("-l", "--legend", dest="legend", action="store_true",
                      help="Activates legend plot.")
    parser.add_option("-o", "--output-path", dest="outputpath", metavar="PATH",
                      help="PATH specifies the output path for generated files. [Default: ./]")

    (options, args) = parser.parse_args()

    if len(sys.argv) == 1:
        parser.print_help()
        exit(0)

    if options.dataset:
        csvFileName = options.dataset
        if not os.path.exists(csvFileName):
            print "[Error] Dataset does not exist"
            exit(0)
    elif options.legend:
        options.slicetiny = False
        options.slicenormal = False
        options.slicebig = False
        options.daily = False
        options.dailystatistics = False
    else:
        print "[Error] No dataset given"
        exit(0)
    if options.config:
        configFileName = options.config
    if options.slicetiny:
        if not os.path.exists(options.slicetiny):
            print "[Error] " + options.slicetiny + " does not exist"
            exit(0)
    if options.slicenormal:
        if not os.path.exists(options.slicenormal):
            print "[Error] " + options.slicenormal + " does not exist"
            exit(0)
    if options.slicebig:
        if not os.path.exists(options.slicebig):
            print "[Error] " + options.slicebig + " does not exist"
            exit(0)
    if options.outputpath:
        outPath = options.outputpath
        if not os.path.exists(outPath):
            print "[Error] Output path does not exist."
            exit(0)

    if not os.path.exists(configFileName):
        print "[Error] Config file \'" + configFileName + "\' does not exist."
        exit(0)


    config = configparser.ConfigParser()
    config.read(configFileName)

    ## generics colors
    global genericsColors
    r = lambda: random.randint(0, 255)
    for g in config.items("generics"):
        if g[1]:
            tempString = '{"data": ' + g[1] + '}'
            tempGenerics = json.loads(tempString)

            for n in tempGenerics['data']:
                label = n[0]
                columnName = n[1]
                color = n[2]
                marker = n[3]

                if g[0] == "cgm":
                    genericsColors['cgmGenerics'][columnName] = ('#%02X%02X%02X' % (r(), r(), r()))
                if g[0] == "boluscalculation":
                    genericsColors['bolusCalculationGenerics'][columnName] = ('#%02X%02X%02X' % (r(), r(), r()))
                if g[0] == "bolus":
                    genericsColors['bolusGenerics'][columnName] = ('#%02X%02X%02X' % (r(), r(), r()))
                if g[0] == "basal":
                    genericsColors['basalGenerics'][columnName] = ('#%02X%02X%02X' % (r(), r(), r()))
                if g[0] == "symbol":
                    genericsColors['symbolGenerics'][columnName] = ('#%02X%02X%02X' % (r(), r(), r()))

    if csvFileName:
        dataset = parseDataset(csvFileName)
    limits = findLimits(dataset, config)

    numberOfPlots = 0.0
    progressCounter = 0.0

    headLineDailyStatistics = ""
    headLineDaily = ""
    headLineTinySlices = ""
    headLineNormalSlices = ""
    headLineBigSlices = ""


    if options.dailystatistics:
        firstDate = dateParser(dataset[0]['date'], "00:00")
        lastDate = dateParser(dataset[0]['date'], "00:00")
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                numberOfPlots += 1
                if dateParser(tempDate, "00:00") < firstDate:
                    firstDate = dateParser(tempDate, "00:00")
                if dateParser(tempDate, "00:00") > lastDate:
                    lastDate = dateParser(tempDate, "00:00")

        days = str(int(divmod((lastDate - firstDate).total_seconds(), 86400)[0]) + 1)
        headLineDailyStatistics = "Daily Log " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y') + " (" + days + " days)"
    if options.daily:
        firstDate = dateParser(dataset[0]['date'], "00:00")
        lastDate = dateParser(dataset[0]['date'], "00:00")
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                numberOfPlots += 1
                if dateParser(tempDate, "00:00") < firstDate:
                    firstDate = dateParser(tempDate, "00:00")
                if dateParser(tempDate, "00:00") > lastDate:
                    lastDate = dateParser(tempDate, "00:00")

        days = str(int(divmod((lastDate - firstDate).total_seconds(), 86400)[0]) + 1)
        headLineDaily = "Daily Log " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y') + " (" + days + " days)"
    if options.slicetiny:
        datasetTinySlices = parseSlices(options.slicetiny)
        numberOfPlots += len(datasetTinySlices)
        firstDate = dateParser(datasetTinySlices[0]['date'], "00:00")
        lastDate = dateParser(datasetTinySlices[0]['date'], "00:00")
        for s in datasetTinySlices:
            if dateParser(s['date'], "00:00") < firstDate:
                firstDate = dateParser(s['date'], "00:00")
            if dateParser(s['date'], "00:00") > lastDate:
                lastDate = dateParser(s['date'], "00:00")
        headLineTinySlices = "Event Slices (Tiny) " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y')
    if options.slicenormal:
        datasetNormalSlices = parseSlices(options.slicenormal)
        numberOfPlots += len(datasetNormalSlices)
        firstDate = dateParser(datasetNormalSlices[0]['date'], "00:00")
        lastDate = dateParser(datasetNormalSlices[0]['date'], "00:00")
        for s in datasetNormalSlices:
            if dateParser(s['date'], "00:00") < firstDate:
                firstDate = dateParser(s['date'], "00:00")
            if dateParser(s['date'], "00:00") > lastDate:
                lastDate = dateParser(s['date'], "00:00")
        headLineNormalSlices = "Event Slices (Normal) " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y')
    if options.slicebig:
        datasetBigSlices = parseSlices(options.slicebig)
        numberOfPlots += len(datasetBigSlices)
        firstDate = dateParser(datasetBigSlices[0]['date'], "00:00")
        lastDate = dateParser(datasetBigSlices[0]['date'], "00:00")
        for s in datasetBigSlices:
            if dateParser(s['date'], "00:00") < firstDate:
                firstDate = dateParser(s['date'], "00:00")
            if dateParser(s['date'], "00:00") > lastDate:
                lastDate = dateParser(s['date'], "00:00")
        headLineBigSlices = "Event Slices (Big) " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y')
    if options.legend:
        numberOfPlots += 1

    finishedPlotsDailyStatistics = []
    finishedPlotsDaily = []
    finishedNotesDaily = []
    finishedPlotsTinySlices = []
    finishedPlotsNormalSlices = []
    finishedPlotsBigSlices = []
    legendFilename = ""

    print "0.00 %"

    if options.dailystatistics:
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                tempPlot = plot(dataset, config, outPath, dateParser(d['date'], '00:00'), 1440.0, "SLICE_DAILYSTATISTICS", False, limits, True)
                finishedPlotsDailyStatistics.append({'filename': tempPlot['filename'], 'timestamp': dateParser(d['date'], '00:00')})
                finishedNotesDaily.append(tempPlot['dailyNotes'])
                progressCounter += 1
                print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.daily:
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                tempPlot = plot(dataset, config, outPath, dateParser(d['date'], '00:00'), 1440.0, "SLICE_DAILY", False, limits, False)
                finishedPlotsDaily.append({'filename': tempPlot['filename'], 'timestamp': dateParser(d['date'], '00:00')})
                finishedNotesDaily.append(tempPlot['dailyNotes'])
                progressCounter += 1
                print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.slicetiny:
        for s in datasetTinySlices:
            finishedPlotsTinySlices.append({'filename': plot(dataset, config, outPath, dateParser(s['date'], s['time']), float(s['duration']), "SLICE_TINY", False, limits, False)['filename'], 'timestamp': dateParser(s['date'], s['time'])})
            progressCounter += 1
            print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.slicenormal:
        for s in datasetNormalSlices:
            finishedPlotsNormalSlices.append({'filename': plot(dataset, config, outPath, dateParser(s['date'], s['time']), float(s['duration']), "SLICE_NORMAL", False, limits, False)['filename'], 'timestamp': dateParser(s['date'], s['time'])})
            progressCounter += 1
            print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.slicebig:
        for s in datasetBigSlices:
            finishedPlotsBigSlices.append({'filename': plot(dataset, config, outPath, dateParser(s['date'], s['time']), float(s['duration']), "SLICE_BIG", False, limits, False)['filename'], 'timestamp': dateParser(s['date'], s['time'])})
            progressCounter += 1
            print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.legend:
        legendDataset = parseDataset(absPath("legend-dataset-v10.csv"))
        limits = findLimits(legendDataset, config)
        legendFilename = plot(legendDataset, config, outPath, dateParser(legendDataset[0]['date'], legendDataset[0]['time']), 1440.0, "SLICE_DAILY", True, limits, False)['filename']
        progressCounter += 1
        print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"

    generateSymbolsLegend(config, outPath)

    generateDailyPlotListWithNotesTex(config, outPath, finishedPlotsDailyStatistics, finishedNotesDaily)
    generateDailyPlotListTex(config, outPath, finishedPlotsDaily)
    generateTinySlicesPlotListTex(config, outPath, finishedPlotsTinySlices)
    generateNormalSlicesPlotListTex(config, outPath, finishedPlotsNormalSlices)
    generateBigSlicesPlotListTex(config, outPath, finishedPlotsBigSlices)
    generateLegendTex(config, outPath, legendFilename)

    generateHeaderTex(config, outPath, config["fileSettings"].get("headerFileDailyStatistics"), headLineDailyStatistics)
    generateHeaderTex(config, outPath, config["fileSettings"].get("headerFileDaily"), headLineDaily)
    generateHeaderTex(config, outPath, config["fileSettings"].get("headerFileTinySlices"), headLineTinySlices)
    generateHeaderTex(config, outPath, config["fileSettings"].get("headerFileNormalSlices"), headLineNormalSlices)
    generateHeaderTex(config, outPath, config["fileSettings"].get("headerFileBigSlices"), headLineBigSlices)

    ### copy static files to outPath ###
    if absPathOnly() != outPath:
        shutil.copy2(absPath("report.tex"), outPath)
        shutil.copy2(absPath("tex_s_titlePage.tex"), outPath)
        shutil.copy2(absPath("tex_s_legendText.tex"), outPath)

if __name__ == '__main__':
    main()
