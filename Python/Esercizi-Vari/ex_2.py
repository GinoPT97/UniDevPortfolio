# Create a program that reads an input from the user,
# writes the value provided by user on screen,
# and loops for maximum 10 times or until the user types a 0
loops = 0
while (loops < 10):
    a = int(input("Type a number and press ENTER (0 to terminate): "))
    print ("You typed %d" % a)
    if (a == 0):
        print ("Program terminates.")
        break
    else:
        loops = loops + 1
        continue
print ("You made %d loops." % loops)
