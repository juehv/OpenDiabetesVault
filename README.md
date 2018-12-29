OpenDiabetesVault
=================
This branch contains most recent code snippets. 

You need Java 8 with Java FX capabilities to rum. 
The repository provides a Netbeans project for Netbeans 8.2
It's highly recommended to run within the Netbeans environment since the code is very unstable and probably not working.

For using the plotting feature you need to install python 2 with matplotlib 2.0.0 (exact version), otherwise it will produce garbage plots. We recommend using Anaconda on Windows.

The platform is plugin based. It loads the plugins form "export" folder within the project folder.
Source Code for provided Plugins are here:
https://github.com/lucasbuschlinger/BachelorPraktikum

Known for working most of the time:
* Carelink importer in importer tab
* Medical Filter in Slicer tab when setup correctly
* Many Filters are working just fine

Known issues:
* Exporter are not working at all. It seems the gui does not start the exporter code.
* Many issues with plots when python is not in expected versioning.
* Complex filters may crash
* Processing plugins got lost.

# Contribute
If you have too much time and want to fix some errors let me know or just send a pull request :)
