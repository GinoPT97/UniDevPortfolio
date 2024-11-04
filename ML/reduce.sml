fun reduce f e [] = e
| reduce f e ( x :: y ) = f (x , reduce f e y );

reduce (op +) 0 [1,2,3,4];

reduce (op *) 1 [1,2,3,4];

reduce (op -) 0 [1,2,3,4];

let
fun f L ( elem , accum ) = elem / real ( length L ) + accum
val lista = [1.0 ,2.0 ,3.0]
in
reduce (f lista) 0.0 lista
end ;