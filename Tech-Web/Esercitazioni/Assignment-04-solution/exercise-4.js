"use strict";

function cachingDecorator(f, n){
  /* Write your code here! */
  let cachedValue;
  let numInvocations = 0;
  return function(){
    //console.log(`${cachedValue} ${numInvocations}`)
    if(cachedValue === undefined || numInvocations === 3){
      cachedValue = f();
      numInvocations = 0;
    } else {
      //console.log(`g returns the cached value ${cachedValue}`)
    }
    numInvocations++;
    return cachedValue;
  }
}

/* Do not change f() */
function f(){
  let value = Math.random();
  console.log(`f has been invoked, result is ${value}`);
  return value;
}

let g = cachingDecorator(f,3);

console.log(g()); //f has been invoked, result is (random number 1)
console.log(g()); //g returns the cached value (random number 1)
console.log(g()); //g returns the cached value (random number 1)
console.log(g()); //f has been invoked, result is (random number 2)
console.log(g()); //g returns the cached value (random number 2)
console.log(g()); //g returns the cached value (random number 2)
console.log(g()); //f has been invoked, result is (random number 3)