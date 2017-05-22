package de.jhit.opendiabetes.vault.plot.config;

public class PlotteriaConfig {
    private int hmin = 70;
    private int hmax = 180;
    private double barWidth = 0.004;
    private int bgCgmMaxValue = 300;
    private double maxBasalValue = 3.5;
    private int cgmBgHighLimit = 300;
    private int cgmBgLimitMarkerLow = 60;
    private int cgmBgLimitMarkerHigh = 250;
    private int minHrValue = 20;
    private int maxHrValue = 160;
    private int minCgmBgValue = 50;

	private double legendXOffset = -0.05;
    private double legendYOffset = 0.27;
    
    private boolean statisticsFlag = false;
    private boolean filterBg = false;
    private boolean plotCarb = true;
    private boolean plotBolus = true;
    private boolean plotBolusCalculation = false;
    private boolean plotBasal = true;
    private boolean plotBg = true;
    private boolean plotCgm = true;
    private boolean plotHeartRate = true;
    private boolean plotSleep = true;
    private boolean plotSymbols = true;
    private boolean plotLocation = true;
    private boolean plotExercise = true;
    private boolean plotStress = true;

    private String hbgColor = "#d1fff4";
    private String gridColor = "#E6E6E6";
    private String carbBarColor = "#ed1c24";
    private String bolusBarColor = "#177d36";
    private String bolusCalculationColor = "#00ff00";
    private String bgPlotColor = "#9245dd";
    private String cgmPlotColor = "#63a7de";
    private String basalPlotColor = "#FAAC58";
    private String heartRatePlotColor = "#cc0066";
    private String overMaxColor = "#FF0000";
    private String symbolsColor = "#000000";
    private String pumpColor = "#000000";
    private String symbolsBackgroundColor = "#FFE5BA";
    private String cgmBgLimitMarkerColor = "#FFFFFF";
    private String stress1Color = "#d5d5ff";
    private String stress2Color = "#ababff";
    private String stress3Color = "#8181ff";
    private String stress4Color = "#5757ff";
    private String exerciseWalkColor = "#E9C7B9";
    private String exerciseRunColor = "#BBE9B9";
    private String exerciseBicycleColor = "#B9C8E9";
    private String exerciseOtherColor = "#E5B9E9";
    private String lightSleepColor = "#d9d9d9";
    private String deepSleepColor = "#dfdfdf";
    
    // Axis Labels
    private boolean showXaxisLabel = false;
    private String xaxisLabel = "Time of Day [hh:mm]";
    private String bolusLabel = "Bolus/Carb [IE/BE]";
    private String basalLabel = "Basal [IE]";
    private String bgLabel = "BG [mg/dl]";
    private String hrLabel = "HeartRate [Hz]";
    
    private String titelDateFormat = "%a %d. %b";
    
    private String delimiter = ":";

    // Legend Labels
    private String bgLegend = "bg";
    private String cgmLegend = "cgm";
    private String basalLegend = "basal";
    private String heartRateLegend = "heart rate";
    private String carbLegend = "carb";
    private String bolusLegend = "bolus";
    private String bolusCalculatonLegend = "bolus calculation";
    
    // Symbol Labels
    private String exerciseLegend = "exercise";
    private String pumpRewindLegend = "rewind";
    private String pumpKatErrLegend = "katheder problem";

    // Symbol Markers
    private String rewindMarker = "*";
    private String katErrMarker = "p";
    private String exerciseMarker = "^";

    // Locations
    private String locTransitionLabel = "Transition";
    private String locHomeLabel = "Home";
    private String locWorkLabel = "Work";
    private String locFoodLabel = "Food";
    private String locSportsLabel = "Sports";
    private String locOtherLabel = "Other";
    
    private String locTransitionColor = "#6791E6";
    private String locHomeColor = "#9DE667";
    private String locWorkColor = "#F2F4B3";
    private String locFoodColor = "#E6A567";
    private String locSportsColor = "#67E686";
    private String locOtherColor = "#E667E2";
    
    // Exercise
    private String exerciseWalkLabel = "Walk";
    private String exerciseRunLabel = "Run";
    private String exerciseBicycleLabel = "Bicycle";
    private String exerciseOtherLabel = "Other";
    
    // Sleep
    private String lightSleepLabel = "Light Sleep";
    private String deepSleepLabel = "Deep Sleep";
    
    // Linewidths
    private double heartRateLineWidth = 1.0;
    private double basalLineWidth = 1.0;
    private double cgmLineWidth = 2.0;
    private double bgLineWidth = 2.0;
    
    // File Settings
    private String filenamePrefix = "Plot";
    private String filenameDateFormatString = "%d_%m_%y";
    private String fileExtension = ".pdf";
    private String plotListFileDaily = "plotListDaily.tex";
    private String plotListFileSlices = "plotListSlices.tex";
    private String headerFile = "header.tex";
    private String legendFile = "legend.pdf";
}
