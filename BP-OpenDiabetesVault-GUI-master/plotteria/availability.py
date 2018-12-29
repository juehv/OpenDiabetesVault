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
    import configparser, os
    import sys
    import shutil
    import pylab
    import numpy as np
    import random
    from optparse import OptionParser
except ImportError as err:
    print "[ModuleError]", err, "(needed libraries: matplotlib, numpy)"
    exit(0)

csvDataFormat = ['date','time','bgValue','cgmValue','cgmRawValue','cgmAlertValue','glucoseAnnotation','basalValue','basalAnnotation','bolusValue','bolusAnnotation','bolusCalculationValue','mealValue','pumpAnnotation','exerciseTimeValue','exerciseAnnotation','heartRateValue','heartRateVariabilityValue','stressBalanceValue','stressValue','sleepValue','sleepAnnotation','locationAnnotation', 'mlCgmValue', 'pumpCgmPredictionValue', 'otherAnnotation']

parsedCsvHeader = ""

plotCounter = 0

def dateParser(date, time):
    return datetime.datetime.strptime(date + time, '%d.%m.%y%H:%M')

def parseDataset(csvFileName):
    global parsedCsvHeader
    with open(csvFileName, 'rU') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', dialect=csv.excel_tab)
        try:
            header = reader.next()
            parsedCsvHeader = header
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

    datasubset = []

    for d in dataset:
        tempDate = dateParser(d['date'], d['time'])
        if tempDate >= beginDate and tempDate < endDate:
            datasubset.append(d)

    return datasubset

tempColors = {}

def newEntry(cfg, ax, date, index, tag, width):
    r = lambda: random.randint(0, 255)
    tempColor = ""
    if not cfg["colors"].get(tag):
        if not tag in tempColors:
            tempColors[tag] = ('#%02X%02X%02X' % (r(), r(), r()))
        tempColor = tempColors[tag]
    else:
        tempColor = cfg["colors"].get(tag)
    ax.add_patch(
                patches.Rectangle(
                    (date, 0.6 + index),  # (x,y)
                    width,  # width
                    0.8,  # height
                    #edgecolor="#00ff00",
                    facecolor=tempColor,
                    alpha=0.75
                )
            )

def plotAvailable(cfg, ax, plottedHeader, tempDate, dataAvailable):
    for i in range(0, len(plottedHeader)):
        if dataAvailable[i]:
            newEntry(cfg, ax, dates.date2num(tempDate), i, plottedHeader[i],
                     datetime.timedelta(minutes=cfg["params"].getfloat("bucketSize")).total_seconds() / 86400.0)
            dataAvailable[i] = False

def plot(cfg, outPath, dataset, plottedHeader, beginDate, duration):
    global plotCounter

    datasubset = dataSubset(dataset, beginDate, duration)

    fig, ax = plt.subplots()
    ax.xaxis_date()
    ax.xaxis.set_major_formatter(dates.DateFormatter('%d.%m.%Y\n%H:%M'))
    #ax.xaxis.set_major_locator(dates.HourLocator(interval=int((dateParser(datasubset[-1]['date'], '23:59') - dateParser(datasubset[0]['date'], '00:00') + datetime.timedelta(minutes=1)).total_seconds() / 43200.0)))

    #ax.set_xlim(dateParser(datasubset[0]['date'], '00:00'), dateParser(datasubset[0]['date'], '00:00') + datetime.timedelta(minutes=duration-1.0))
    ax.set_xlim(beginDate, beginDate + datetime.timedelta(minutes=duration - 1.0))

    ax.set_ylim(0, len(plottedHeader) + 1)
    ax.yaxis.set_major_locator(MultipleLocator(cfg["params"].getfloat("bucketSize")/1440.0))

    plt.yticks(range(1, len(plottedHeader) + 1), plottedHeader)

    dataAvailable = []
    for i in range(0, len(plottedHeader)):
        dataAvailable.append(False)

    tempDate = beginDate

    if datasubset:
        for d in datasubset:
            if dateParser(d['date'], d['time']) < (tempDate + datetime.timedelta(minutes=cfg["params"].getfloat("bucketSize"))):
                for i in range(0,len(plottedHeader)):
                    if d[plottedHeader[i]]:
                        dataAvailable[i] = True
            else:
                plotAvailable(cfg, ax, plottedHeader, tempDate, dataAvailable)
                tempDate += datetime.timedelta(minutes=cfg["params"].getfloat("bucketSize"))

        ## last case
    plotAvailable(cfg, ax, plottedHeader, tempDate, dataAvailable)

    scale = 1.2
    fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.97, left=0.14)
    fig.set_size_inches(11.69*scale,8.27*scale)
    plt.savefig(os.path.join(outPath, cfg['params'].get('filePrefix') + str(plotCounter) + cfg['params'].get('fileExtension')))
    plt.close()
    plotCounter += 1

def main():
    versionString = "OpenDiabetesVault-Availability-Plot v0.9"
    outPath = os.getcwd()
    csvFileName = ""

    parser = OptionParser(usage="%prog [OPTION] [FILE ..]", version=versionString, description=versionString)

    parser.add_option("-f", "--data-set", dest="dataset", metavar="FILE",
                      help="FILE specifies the dataset for the plot.")
    parser.add_option("-c", "--config", dest="config", metavar="FILE", default="availability_config.ini",
                      help="FILE specifies configuration file for the plot [Default config.json].")
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
    else:
        print "[Error] No dataset given"
        exit(0)
    if options.config:
        configFileName = options.config
        if not os.path.isfile(configFileName):
            input = raw_input("[Config] Config file \'" + configFileName + "\' does not exist. Do you want to create it? (y/n)")
            if input != "y":
                exit(0)
    if options.outputpath:
        outPath = options.outputpath
        if not os.path.exists(outPath):
            print "[Error] Output path does not exist."
            exit(0)

    cfg = configparser.ConfigParser()
    cfg.read(configFileName)

    if not cfg.has_section('params'):
        cfg.add_section('params')
        cfg.set('params', 'bucketSize', '60.0')
        cfg.set('params', 'bucketCount', '24.0')
        cfg.set('params', 'ignoreFields', 'date,time')
        cfg.set('params', 'filePrefix', 'availablility_')
        cfg.set('params', 'fileExtension', '.pdf')
        with open(configFileName, 'wb') as configfile:
            cfg.write(configfile)
    if not cfg.has_section('colors'):
        cfg.add_section('colors')
        cfg.set('colors', '; Example: cgmValue', '#552900')
        with open(configFileName, 'wb') as configfile:
            cfg.write(configfile)

    dataset = parseDataset(csvFileName)

    global parsedCsvHeader

    plottedHeader = list(set(parsedCsvHeader) - set(re.split(",", cfg["params"].get("ignoreFields"))))

    beginDate = dateParser(dataset[0]['date'], '00:00')
    duration = cfg["params"].getfloat("bucketSize") * cfg["params"].getfloat("bucketCount")

    tempDate = beginDate
    numberOfPlots = 0
    while (tempDate < dateParser(dataset[-1]['date'], '23:59') + datetime.timedelta(minutes=1.0)):
        numberOfPlots += 1
        tempDate += datetime.timedelta(minutes=duration)

    tempDate = beginDate
    counter = 0.0
    print "0.00 %"
    while(tempDate < dateParser(dataset[-1]['date'], '23:59') + datetime.timedelta(minutes=1.0)):
        plot(cfg, outPath, dataset, plottedHeader, tempDate, duration)
        tempDate += datetime.timedelta(minutes=duration)
        counter += 1.0
        print str('{0:.2f}'.format(float(counter / numberOfPlots) * 100)) + " %"

if __name__ == '__main__':
    main()