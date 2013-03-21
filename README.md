ImageMagickCompareUtil
======================

ImageMagickCompareUtil is an attempt to automate image comparison. This can be used on comparing a website to ensure that no changes has occurred to the layout of the website. At present the implementation is naive, but it is a starting point.

This is work in progress.

One to one comparisons would work without any issues.

It will work on web pages that have different heights or different widths, provided that one is a sub-set of the other. For example:

```
Image A: 300x400 px
Image B: 305x400 px // Works as A is a sub-image (Size wise) of B
```

OR

```
Image A: 400x300 px
Image B: 305x300 px // Works as B is a sub-image (Size wise) of A
```

It will NOT work on web pages with dynamic elements of varying sizes or varying width and height. For example:

```
Image A: 300x400 px
Image B: 295x405 px // Cannot work as width is less, but height is greater... which means B cannot be sub-image of A or vice-versa
```

Pre-requisites
==============

You must have ImageMagick installed on your machine.

http://www.imagemagick.org for more details

Usage
=====

All the settings of are configured in a properties file named settings.properties. Here, you can set the ImageMagick binary path, expected / actual files path, and report format type.

Modify settings.properties pathToImageMagickBinary and point it to your ImageMagick's binary path as appropriate.

By default the screenshots are saved at a folder called screenshot in the root folder of this project.

You can write a WebDriver test (Or any other screen capture tool) to capture screenshots and then save them at screenshot/actual folder.

Create a screenshot/expected (Or any other folder to your liking) folder and copy the files over as your first expected screenshots. Run the test again, which will overwrite your previous screenshot.

Now create a screenshot/diff folder (Leave it empty).

You can then run ImageMagickCompareUtil.

Results
=======

Currently the results support three formats: XML, CSV and HTML.

The results are saved in the screenshot folder to a file called results.csv by default. If you want to output to HTML report, change the report type to be HTML and the corresponding output file to be results.html. The same can be done with XML report.

Diff results (Difference) are stored in the diff folder specified.
