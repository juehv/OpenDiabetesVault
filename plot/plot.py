import sys
import csv
import matplotlib.pyplot as plt
import matplotlib.dates as dates
import matplotlib.lines as lines
import matplotlib.transforms as transforms
import datetime
import pylab
import numpy as np

def parse(data):
    res = []
    prevDate = 0;
    dateIndex = -1;
    with open(data, 'rU') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', dialect=csv.excel_tab)
        #for row in reader:
        striped = [[x.strip() for x in row] for row in reader]
        for row in striped:
            date, time, bgValue, cgmValue, cgmAlertValue, basalValue, bolusValue, carbValue, pumpAnnotation, exerciseTimeValue, exerciseTypeValue = row
            try:
                timestamp = datetime.datetime.strptime(date+time, '%d.%m.%y%H:%M')
                currentDate = datetime.datetime.strptime(date, '%d.%m.%y')
                if currentDate != prevDate:
                    prevDate = currentDate
                    dateIndex += 1
                    res.append([])

                res[dateIndex].append((dates.date2num(timestamp), bgValue, cgmValue, cgmAlertValue, basalValue, bolusValue, carbValue, pumpAnnotation, exerciseTimeValue, exerciseTypeValue))

            except ValueError:
                print "ValueError"
    return res

plotCount = 0

def plot(data,config,plotList):
    global plotCount
    ### data preprocessing ###
    bgValuesX = []
    bgValuesY = []
    cgmValuesX = []
    cgmValuesY = []
    cgmAlertValuesX = []
    cgmAlertValuesY = []
    cgmValuesRedX = []
    cgmValuesRedY = []
    cgmAlertValuesRedX = []
    cgmAlertValuesRedY = []
    
    bolusValuesX = []
    carbValuesX = []    
    bolusValuesY = []
    carbValuesY = []     
    basalValuesX = []
    basalValuesY = []
    
    exerciseX = []
    exerciseY = []
    pumpRewindX = []
    pumpRewindY = []
    pumpKatErrX = []
    pumpKatErrY = []

    dateValue = 0

    maxBarValue = 0.0

    for d in data:
        if d[1] != "":
            bgValuesX.append(d[0])
            if float(d[1]) > config.bgCgmMaxValue:
                bgValuesY.append(config.cgmBgHighLimit-1)
            else:
                bgValuesY.append(d[1])
        if d[2] != "":
            cgmValuesX.append(d[0])
            if float(d[2]) > config.bgCgmMaxValue:
                cgmValuesY.append(config.cgmBgHighLimit-1)
                cgmValuesRedX.append(d[0])
                cgmValuesRedY.append(config.cgmBgHighLimit-1)
            else:
                cgmValuesY.append(d[2])
        if d[3] != "":
            cgmAlertValuesX.append(d[0])
            if float(d[3]) > config.bgCgmMaxValue:
                cgmAlertValuesY.append(300)
                cgmAlertValuesRedX.append(d[0])
                cgmAlertValuesRedY.append(config.cgmBgHighLimit-1)
            else:
                cgmAlertValuesY.append(d[3])
        if d[4] != "":
            basalValuesX.append(d[0])
            basalValuesY.append(d[4])
        if d[5] != "":
            bolusValuesX.append(d[0])
            bolusValuesY.append(d[5])
        if d[6] != "":
            carbValuesX.append(d[0])
            carbValuesY.append(d[6])
        if d[7] == "PUMP_REWIND":
            pumpRewindX.append(d[0])
            pumpRewindY.append(1)
        if d[7] == "PUMP_KATERR":
            pumpKatErrX.append(d[0])
            pumpKatErrY.append(1)
        if d[8] != "":
            exerciseX.append(d[0])
            exerciseY.append(1)
        dateValue = d[0]

    datemin = dates.num2date(dateValue, tz=None).replace(hour=00,minute=00)
    datemax = dates.num2date(dateValue, tz=None).replace(hour=23, minute=59, second=59)

    # extend basal to reach to the end of the plot
    if len(basalValuesY) > 0:
        basalValuesX.append(dates.date2num(datemax))
        basalValuesY.append(basalValuesY[len(basalValuesY)-1])

    ### Merge barplot values ###
    mergeTimespan = 30

    mergedBolusX = []
    mergedBolusY = []

    mergedCarbX = []
    mergedCarbY = []

    dateI = datemin
    currentMerge = 0.0
    currentCount = 0
    for i in range(0,48):
        ## merge bolus ##
        for i, t in enumerate(bolusValuesX):
            if t > dates.date2num(dateI) and t < dates.date2num(dateI+datetime.timedelta(minutes = mergeTimespan)):
                #print i, dates.num2date(t), bolusValuesY[i]
                currentMerge += float(bolusValuesY[i])
                currentCount += 1
        if currentCount > 0:
            mergedBolusX.append(dates.date2num(dateI + datetime.timedelta(minutes=mergeTimespan / 2)))
            mergedBolusY.append(currentMerge / currentCount)
        currentMerge = 0.0
        currentCount = 0

        ## merge carb ##
        for i, t in enumerate(carbValuesX):
            if t > dates.date2num(dateI) and t < dates.date2num(dateI+datetime.timedelta(minutes = mergeTimespan)):
                #print i, dates.num2date(t), bolusValuesY[i]
                currentMerge += float(carbValuesY[i])
                currentCount += 1
        if currentCount > 0:
            mergedCarbX.append(dates.date2num(dateI + datetime.timedelta(minutes=mergeTimespan / 2)))
            mergedCarbY.append(currentMerge / currentCount)
        currentMerge = 0.0
        currentCount = 0
        dateI = dateI + datetime.timedelta(minutes=mergeTimespan)


    ########## Prepare Canvas ##########
    fig = plt.figure()
    fig.suptitle(dates.num2date(data[0][0], tz=None).strftime(config.titelDateFormat), fontsize=14, fontweight='bold', y=1.0)
    # TODO use self explanatory names instead of axN
    ax1 = plt.subplot2grid((21, 1), (13, 0), rowspan=8, colspan=1)
    ax2 = plt.subplot2grid((21, 1), (1, 0), rowspan=12, colspan=1, sharex=ax1)
    ax3 = plt.subplot2grid((21, 1), (0, 0), rowspan=1, colspan=1, sharex=ax1)
    ax1_left = ax1.twinx()


    # hiding xticks and labels
    for tic in ax2.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False
    for tic in ax3.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False

    #ax3.xaxis.set_visible(False)
    ax3.yaxis.set_visible(False)

    ax1.grid(color=config.gridColor, linestyle='-')
    ax1.set_axisbelow(True)
    ax2.grid(color=config.gridColor, linestyle='-')
    ax2.set_axisbelow(True)
    ax3.grid(color=config.gridColor, linestyle='-')
    ax3.set_axisbelow(True)

    al = 7
    arrowprops = dict(clip_on=False,  # plotting outside axes on purpose
                      headlength=al,  # make end arrowhead the whole size of arrow
                      headwidth=al,  # in points
                      facecolor='k')
    kwargs = dict(
        xycoords='axes fraction',
        textcoords='offset points',
        arrowprops=arrowprops,
    )
    
    if (config.showXaxisLabel):
        ax1.set_xlabel(config.xaxisLabel)
        ax1.xaxis.set_label_position('bottom')

    ax3.annotate("", (0, 1), xytext=(0, -al), **kwargs)
    ax3.annotate("", (1, 1), xytext=(0, -al), **kwargs)

    scale = 1.5
    fig.set_size_inches(8.27*scale, 11.69/3*scale)
    
    # delete space between the two subplots to make it look like one
    fig.subplots_adjust(hspace=0)
     
    ########## Basal, Bolus, Carb Plot ##########
    ax1.set_ylim(0, config.maxBarValue)
    ax1_left.set_ylim(0, config.maxBasalValue)

    # remove overlapping yaxis ticks
    yticksax1 = ax1.yaxis.get_major_ticks()
    yticksax1[-1].label1.set_visible(False)

    yticksax2 = ax1_left.yaxis.get_major_ticks()
    yticksax2[-1].label2.set_visible(False)

    basalPlot, = ax1_left.plot(basalValuesX, basalValuesY, linewidth=1, color=config.basalPlotColor, drawstyle='steps-post', label='basal')  # Plot basalValues
    ax1_left.set_ylabel(config.basalLabel)
    
    carbBars = ax1.bar([dates.num2date(t)-datetime.timedelta(minutes = 5) for t in mergedCarbX], mergedCarbY, config.barWidth, color=config.carbBarColor, label='carb')
    bolusBars = ax1.bar([dates.num2date(t)+datetime.timedelta(minutes = 5) for t in mergedBolusX], mergedBolusY, config.barWidth, color=config.bolusBarColor, label='bolus')
    ax1.xaxis_date()
    ax1.xaxis.set_major_formatter(dates.DateFormatter('%H:%M'))
    ax1.xaxis.set_major_locator(dates.HourLocator(interval=2))

    ax1.set_ylabel(config.bolusLabel)
    
    ax1.spines['top'].set_visible(False)
    ax1.xaxis.set_ticks_position('bottom')
    ax1.set_xlim(datemin, datemax)
    
    ax1_left.spines['top'].set_visible(False)

    ########## CGM, BG PLOT ##########
    # TODO dynamic low limit
    ax2.set_ylim(config.minCgmBgValue,config.cgmBgHighLimit)

    bgPlot, = ax2.plot(bgValuesX, bgValuesY, linewidth=2, color=config.bgPlotColor)        # Plot bgValues
    ax2.plot(bgValuesX, bgValuesY, 'ro', color=config.bgPlotColor)               # Plot bgValues (datapoints)
    cgmPlot, = ax2.plot(cgmValuesX, cgmValuesY, linewidth=2, color=config.cgmPlotColor)  # Plot cgmValues
    #ax2.plot(cgmValuesRedX, cgmValuesRedY, linewidth=2, color=config.overMaxColor, label='cgm')  # Plot cgmValues
    ax2.plot(cgmAlertValuesX, cgmAlertValuesY, 'ro', color=config.cgmPlotColor)  # Plot cgmAlertValues (datapoints)
    ax2.plot(cgmAlertValuesRedX, cgmAlertValuesRedY, 'ro', color=config.overMaxColor)  # Plot cgmAlertValues (datapoints)

    ax2.spines['bottom'].set_visible(True)
    ax2.spines['top'].set_visible(False)
    # turn off ticks where there is no spine
    ax2.yaxis.set_ticks_position('left')
    #ax2.xaxis.set_visible(False)
    ax2.set_ylabel(config.bgLabel)

    # background field specified by hmin/hmax
    ax2.axhspan(config.hmin, config.hmax, facecolor=config.hbgColor, alpha=0.5)
    ax2.axhspan(config.cgmBgLimitMarkerLow, config.cgmBgLimitMarkerHigh, facecolor=config.cgmBgLimitMarkerColor, alpha=0.5)

    ########## Symbols ##########
    pumpRewindPlot, = ax3.plot(pumpRewindX, pumpRewindY, config.rewindMarker, color=config.pumpColor)
    pumpKatErrPlot, = ax3.plot(pumpKatErrX, pumpKatErrY, config.katErrMarker, color=config.symbolsColor)
    exercisePlot, = ax3.plot(exerciseX, exerciseY, config.exerciseMarker, color=config.symbolsColor)

    ax3.spines['bottom'].set_visible(True)
    ax3.spines['top'].set_visible(False)

    # background field for the symbols
    ax3.axhspan(0.9, 1.1, facecolor=config.symbolsBackgroundColor, alpha=0.5)

    ax3.set_ylim([0.9, 1.1])

    ## offset calc for legend ##
    axbox = ax1.get_position()
    #x_value = .0005
    #y_value = .080
    x_value = -0.05
    y_value = 0.075

    ########## Legend ##########
    handles = [bgPlot, cgmPlot, basalPlot]
    labels = [config.bgLegend, config.cgmLegend, config.basalLegend]
    
    if(len(bolusBars) > 0):
        handles.append(bolusBars)
        labels.append(config.bolusLegend)
    if(len(carbBars) > 0):
        handles.append(carbBars)
        labels.append(config.carbLegend)
    
    # TODO seperate legend at top line once per page (show always all possible symbols) 
#    if (len(exercisePlot.get_xdata()) > 0):
#        handles.append(exercisePlot)
#        labels.append(config.exerciseLegend)
#    if (len(pumpRewindPlot.get_xdata()) > 0):
#        handles.append(pumpRewindPlot)
#        labels.append(config.pumpRewindLegend)
#    if (len(pumpKatErrPlot.get_xdata()) > 0):
#        handles.append(pumpKatErrPlot)
#        labels.append(config.pumpKatErrLegend)

    #fig.legend(handles, labels, loc=(axbox.x0 + x_value, axbox.y0 + y_value), fontsize=10)
    fig.legend(handles, labels, loc="upper left", bbox_to_anchor=[axbox.x0 + config.legendXOffset, axbox.y0 + config.legendYOffset], fontsize=10)


    plt.subplots_adjust(top=0.95, bottom=.04, right=.95, left=0.05, hspace=0, wspace=0)
    #plt.subplots_adjust(top=1, bottom=.08, right=.96, left=0.05, hspace=0, wspace=0)
    #plt.subplots_adjust(top=0.9, bottom=0.15, right=0.90, left=0.125, hspace=0, wspace=0)

    fileName = config.filenamePrefix + dates.num2date(data[0][0], tz=None).strftime(config.filenameDateFormatString) + config.fileExtension
    plt.savefig(fileName)
    #plt.show()
    plt.close()
    #plotList.write("\\vfill\n\\centerline{\\includegraphics[width=\\textwidth,height=\\textheight,keepaspectratio]{" + fileName + "}}\
    if divmod(plotCount, 3)[1] == 0:
        plotList.write("\\newpage\n\\input{header}\n")
    plotList.write("\\vspace{0.3em}\n\\centerline{\\includegraphics[scale=0.57,keepaspectratio]{" + fileName + "}}\n")
    plotCount+=1

def main():
    if len(sys.argv) != 2:
        print "usage plot.py <csv>"
        exit(0)

    csvfile = sys.argv[1]
    data = parse(csvfile)

    maxBasal = 0.0
    maxBV = 0.0
    minCgmBg = 300.0
    firstDate = dates.num2date(data[0][0][0])
    lastDate = dates.num2date(data[0][0][0])

    for dat in data:
        for d in dat:
            if d[4] != "" and float(d[4]) > maxBasal:
                maxBasal = float(d[4])
            if d[5] != "" and float(d[5]) > maxBV:
                maxBV = float(d[5])
            if d[6] != "" and float(d[6]) > maxBV:
                maxBV = float(d[6])
            if d[1] != "" and float(d[1]) < minCgmBg:
                minCgmBg = float(d[1])
            if d[2] != "" and float(d[2]) < minCgmBg:
                minCgmBg = float(d[2])
            lastDate = dates.num2date(d[0])

    minCgmBg = int(minCgmBg/10)*10

    ### Config ###
    #TODO export in separate file
    class Config:
        hmin = 70
        hmax = 180
        barWidth = 0.0058
        bgCgmMaxValue = 300
        maxBarValue = maxBV
        minCgmBgValue = minCgmBg
        maxBasalValue = 3.5
        cgmBgHighLimit = 300
        cgmBgLimitMarkerLow = 60
        cgmBgLimitMarkerHigh = 250

        legendXOffset = -0.05
        legendYOffset = 0.29

        hbgColor = '#d1fff4'
        gridColor = '#E6E6E6'
        carbBarColor = '#ed1c24'
        bolusBarColor = '#177d36'
        bgPlotColor = '#9245dd'
        cgmPlotColor = '#63a7de'
        basalPlotColor = '#FAAC58'
        overMaxColor = '#FF0000'
        symbolsColor = '#000000'
        pumpColor = '#000000'
        symbolsBackgroundColor = '#FFE5BA'
        cgmBgLimitMarkerColor = '#FFFFFF'

        ## Axis Labels ##
        showXaxisLabel = False
        xaxisLabel = 'Time of Day [hh:mm]'
        bolusLabel = 'Bolus/Carb [IE/BE]'
        basalLabel = 'Basal [IE]'
        bgLabel = 'BG[mg/dl]'
        
        titelDateFormat = '%a %d. %b'

        ## Legend Labels ##
        bgLegend = 'bg'
        cgmLegend = 'cgm'
        basalLegend = 'basal'
        carbLegend = 'carb'
        bolusLegend = 'bolus'
        
        ## Symbol Labels ##
        exerciseLegend = 'exercise'
        pumpRewindLegend = 'rewind'
        pumpKatErrLegend = 'katheder problem'

        ## Symbol Markers ##
        rewindMarker = '*'
        katErrMarker = 'p'
        exerciseMarker = '^'

        filenamePrefix = 'Plot'
        filenameDateFormatString = '%d_%m_%y'
        fileExtension = '.png'

        plotListFile = 'plotList.tex'
        headerFile = 'header.tex'
        legendFile = 'legend.pdf'

    ## Symbol Legend ##
    rewindPlot = lines.Line2D([], [], marker=Config().rewindMarker, linestyle='None', color=Config().symbolsColor)
    katErrPlot = lines.Line2D([], [], marker=Config().katErrMarker, linestyle='None', color=Config().symbolsColor)
    exercisePlot = lines.Line2D([], [], marker=Config().exerciseMarker, linestyle='None', color=Config().symbolsColor)
    handles = [rewindPlot, katErrPlot, exercisePlot]
    labels = [Config().pumpRewindLegend, Config().pumpKatErrLegend, Config().exerciseLegend]

    figLegend = pylab.figure()
    legend = pylab.figlegend(handles, labels, 'center', numpoints=1, fontsize=10, ncol=3, columnspacing=2)
    figLegend.canvas.draw()

    bbox = legend.get_window_extent().transformed(figLegend.dpi_scale_trans.inverted())
    ll, ur = bbox.get_points()
    x0, y0 = ll
    x1, y1 = ur
    w, h = x1 - x0, y1 - y0
    x1, y1 = x0 + w * 1, y0 + h * 1.1
    bbox = transforms.Bbox(np.array(((x0, y0), (x1, y1))));

    plt.savefig(Config().legendFile, bbox_inches=bbox)
    plt.close()

    ## tex header file ##
    days = str(int(divmod((lastDate - firstDate).total_seconds(),86400)[0]) + 1)
    headLine = "Daily Log " + firstDate.strftime('%d.%m.') + "-" + lastDate.strftime('%d.%m.%y') + " (" + days + " days)"
    fileHeader = open(Config().headerFile, 'w')

    fileHeader.write("\\noindent \\large{\\textbf{" + headLine + "}} \\hfill \\small{Page \\thepage/\\pageref{LastPage}}\n\n\\vspace{0.5em}\n")
    fileHeader.write("\centerline{\\includegraphics[width=200pt,height=200pt,keepaspectratio]{" + Config().legendFile + "}}\n\\vspace{0.1em}")    
    fileHeader.close()

    ## tex file ##
    filePlotList = open(Config().plotListFile, 'w')

    for d in data:
        plot(d, Config(), filePlotList)

    filePlotList.close()

if __name__ == '__main__':
    main()
