import numpy as np
import matplotlib.pyplot as plt
x = np.arange(0,100.5,0.5) # Generate x-axis values
y = 2.0*np.sqrt(x) # Calculate y-values
fig = plt.figure() # Create figure
ax = fig.add_axes([0.07, 0.07, 0.9, 0.9]) # Create axes
ax.plot(x,y) # Plot data on axes
plt.show() # Display plot to screen

