import numpy as np
import matplotlib.pyplot as plt
x = np.arange(0,100.5,0.5) # Generate x-axis values
y = 2.0*np.sqrt(x) # Calculate y-values
plt.plot(x,y) # Create figure and axis objects
plt.show() # Display plot to screen
