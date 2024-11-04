import scipy.stats as stat
import numpy as np
import matplotlib.pyplot as plt
y = stat.norm.rvs(1, 1, size = 1000)
ys = stat.describe(y)
#y = stat.norm.rvs(5, 1, size = 1000)
#print(stat.describe(y))
#y = stat.norm.rvs(5, 5, size = 1000)
#print(stat.describe(y))