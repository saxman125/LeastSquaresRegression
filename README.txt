Least-Squares Regression
Alan Yang & Vikram Anjur

Performs Least-Squares Regression, taking an input string (equation form) and
fitting a curve of that form to a text file of data points, with the smallest chi squared
value. Uses a matrix projection approach. Output includes an equation of desired form,
a chi squared value, and a graph of the curve (in 2D, default xy plane)

Input text file format:

1. equation format
2. list of variables
3. desired x,y,z, etc. values spaced by tabs.

Example 1:
y=Ax+B
x,y
2.3	4
5	2.2
7.3	9.4
...	...

Example 2 (A sphere of form x^2+y^2z^2+Ax+By+C=0):
C+Ax+By=-x^2-y^2
x,y,z
2.3	4	5.3
5	2.2	2.6
7.3	9.4	9.4
...	...	...
