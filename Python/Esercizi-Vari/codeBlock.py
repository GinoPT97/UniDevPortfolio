def f(a):
    if (a == 5):
        print ("number equal to to 5")
        b = 1
    else:
        print ("number different from 5"); b = 0
    print ("b = ", b)    # This instruction is executed anyway
    return b
f(3)
f(5)

