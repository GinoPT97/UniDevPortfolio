# Write the same program using find() method and while() loop.

myString = input ("Give me the string with commas: ")
print ("You gave me string: ", myString)
i = 0
while i < len(myString):
    found = myString.find(",", i)
    if found >= 0:
        print ("Comma found in position: ", found)
        i = found + 1
    else:
        break