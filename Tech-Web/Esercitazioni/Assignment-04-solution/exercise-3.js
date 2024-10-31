"use strict"; 

for(let n = 2; n <= 10000; n++){
  let steps = 0, current = n;
  while(current != 1){
    steps++;
    if(current%2 === 0){
      current = current/2;
    } else {
      current = current*3+1
    }
  }
  console.log(`Collatz proved for n=${n}: sequence converges to 1 in ${steps} steps`);
}