fun filter f [] = []
  | filter f ( x :: y ) = if f(x) then x ::( filter f y )
                                  else filter f y;

fun map f [] = []
  | map f ( x :: y ) = f ( x )::( map f y );

fun reduce f e [] = e
  | reduce f e ( x :: y ) = f (x , reduce f e y );

fun fatt x = if x = 0 then 1 else x * fatt(x-1);

fn (x,L) => if L=[] then false else let val (y::R) = L in x=y end;

fn x => fn L => if L=[] then false else let val (y::R) = L in x=y end;

fun f x [] = false
| f x (y::z) = x=y orelse f x z;

fun f x [] = 0
| f x (y::z) = x=y orelse f x z;

fun f (x,[]) = 0
  | f (x,y::z) = x=y orelse f (x,z);