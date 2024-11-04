import numpy as np

thres = 40
csv_file = open("votes.csv", "r")
lines = csv_file.readlines()[1:] # Ignore the first line of the file
votes = np.empty([len(lines), 2])
i = 0
for line in lines:
    line = line.rstrip('\n')
    dataItem = line.split(',')
    age = int(dataItem[2])
    vote = int(dataItem[3])
    votes[i,:] = [age, vote]
    i += 1
csv_file.close()

#let's calculate the mean values
meanAll = 0
meanFilt = 0
j = 0
for i in range(len(votes)):
    meanAll += votes[i, 1]
    if (votes[i, 0] > thres):
        meanFilt += votes[i, 1]
        j += 1
meanAll /= (i + 1)
meanFilt /= j
print ("The average on all is: ", meanAll)
print ("The average on students older than ", thres,
       " is: ", meanFilt)