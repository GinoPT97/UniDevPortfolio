import numpy as np
import matplotlib.pyplot as plt
x = np.arange(0,100.5,5) # Generate x-axis values
y1 = 200.0*np.sqrt(x) # Calculate y-values
y2 = 7*1.06**x # Calculate y-values
fig = plt.figure() # Create figure
plt.rc('xtick', labelsize = 15)
plt.rc('ytick', labelsize = 15)
ax = fig.add_axes([0.15, 0.15, 0.75, 0.75]) # Create axes
ax.plot(x, y1, c = 'r', ls = '--', lw = 2,
        marker = 'x', ms = 15, mew = 3)
ax.plot(x, y2, c = 'b', ls = '-.', lw = 2,
        marker = 'o', ms = 10)
ax.set_title("Let's do this plot", size = 15)
ax.set_xlabel('This is X', size = 15)
ax.set_ylabel('This is Y', size = 15)
ax.grid(axis = 'both')
plt.legend((r'$200\sqrt{x}$', r'$7\times{1.06}^x$'),
           prop = dict(size = 15), loc = 0)
fig = plt.gcf() # Get reference to current figure
fig.savefig('saved-plot.png')
plt.show() # Display plot to screen

