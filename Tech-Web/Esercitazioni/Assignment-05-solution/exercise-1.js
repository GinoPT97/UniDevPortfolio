/* i) Person constructor function */
function Person(firstName, lastName) {
  this.firstName = firstName;
  this.lastName = lastName;
}

/* ii) create two new Person objects */
let p1 = new Person("Kyle", "Broflowsky");
let p2 = new Person("Stan", "Marsh");

/* iii) Add a greet methods to all Persons */
Person.prototype.greet = function(){
  console.log(`Hello, I'm ${this.firstName} ${this.lastName}`);
}

/* iv) What is happening under the hood? */
/*
  - We invoke p1.greet()
  - The JavaScript engine checks whether p1 has a greet property
  - The greet property is not defined in p1 (it is not set in the constructor).
  - Since greet is not defined in p1, the JavaScript engine starts navigating the prototype chain
  - p1.[[Prototype]] is the default prototype associated with constructor functions (i.e., Person.prototype)
  - In step iii) we added the greet method to Person.property (Line 12)
  - The JavaScript engine finds the desired greet property in Person.prototype and invokes it
  - Note that the value of this depends on the object on which the function is called (i.e., p1!)
*/

/* v) Invoke the method on each person object and check it works */
p1.greet();
p2.greet();

/* vi) create a new constructor Student */
function Student(firstName, lastName, degreeProgram){
  Person.call(this, firstName, lastName);
  this.degreeProgram = degreeProgram;
}
/* Make sure to set the [[Prototype]] of Student.prototype to Person.prototype */
Object.setPrototypeOf(Student.prototype, Person.prototype);

/* vii) Create two new student objects */
let s1 = new Student("Tsu", "Ssu", "Computer Science");
let s2 = new Student("Tsu", "Li", "Computer Science");

/* viii) invoke greet on the newly created objects */
s1.greet();
s2.greet();

/* ix) What is happening under the hood? */
/*
  - We invoke s1.greet()
  - The JavaScript engine checks whether s1 has a greet property
  - The greet property is not defined in s1 (it is not set in the constructor).
  - Since greet is not defined in s1, the JavaScript engine starts navigating the prototype chain
  - s1.[[Prototype]] is the default prototype associated with constructor functions (i.e., Student.prototype)
  - Student.prototype does not include a greet property (it only hase a constructor property pointing back to Student)
  - The JavaScript engine navigates to the next level in the prototype chain, i.e., Student.prototype.[[Prototype]]
  - The [[Prototype]] of Student.prototype was set in Line 38 to Person.prototype
  - The JavaScript engine looks for a greet property in Person.prototype 
  - In step iii) we added the greet method to Person.property (Line 12)
  - The JavaScript engine finds the desired greet property in Person.prototype and invokes it
*/

/* x) Override the greet method */
Student.prototype.greet = function(){
  console.log(`Hi! I'm ${this.firstName} ${this.lastName} and I'm enrolled in the ${this.degreeProgram} degree program.`);
}

/* xi) Invoke greet on all objects */
s1.greet();
s2.greet();
p1.greet();
p2.greet();
p3.greet();

/* xii) Explain step by step what happens under the hood */
/* 
  - WHEN s1.greet is invoked
    - We invoke s1.greet()
    - The JavaScript engine checks whether s1 has a greet property
    - The greet property is not defined in s1 (it is not set in the constructor).
    - Since greet is not defined in s1, the JavaScript engine starts navigating the prototype chain
    - s1.[[Prototype]] is the default prototype associated with constructor functions (i.e., Student.prototype)
    - In step x) we added the greet method to Student.prototype (Line 64)
    - The JavaScript engine finds the desired greet property in Student.prototype and invokes it (no need to continue going up the prototype chain)
  - WHEN p1.greet is invoked
    - We invoke p1.greet()
    - The JavaScript engine checks whether p1 has a greet property
    - The greet property is not defined in p1 (it is not set in the constructor).
    - Since greet is not defined in p1, the JavaScript engine starts navigating the prototype chain
    - p1.[[Prototype]] is the default prototype associated with constructor functions (i.e., Person.prototype)
    - In step iii) we added the greet method to Person.property (Line 12)
    - The JavaScript engine finds the desired greet property in Person.prototype and invokes it
*/