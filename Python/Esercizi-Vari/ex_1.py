# Create a program that reads an input from the user and
# controls if such input is an integer and, in this case,
# prints if the integer is positive, negative, or zero
a = input("Dammi il numero: ")
try:
    a = int(a)
except ValueError:
    print("Non è int")
print("Il numero", a, "è intero")
if (a > 0):
    print("Il numero", a, "è positivo")
elif (a < 0):
    print("Il numero", a, "è negativo")
else:
    print("Il numero è 0")

