# Write a program that finds all the occurrences of the character ‘,’
# in a string and prints their position on screen.
# The string must be provided by the user
myString = input ("Give me the string with commas: ")
print ("You gave me the string: ", myString)
myList = []
for i in range (len(myString)):
    if (myString[i] == ','):
        print ("Comma found in position: ", i, "\n")
        myList.append(i)
print (myList)


