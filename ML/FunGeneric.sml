fun concat ([],L2) = L2 
  | concat(x::xs, L2) = x::concat(xs,L2);

 fun append (l,i) = case l of 
     [] => [i]
   |h::t => h::append(t,i);

fun member(l,i) = 
    case l of
    [] => false
  | h::t => if h = i then true else member(t,i);

fun last [] = raise Empty 
  |last [x] = x
  |last (h::t) = last t;

fun tl [] = raise Empty | tl(h::t) = t; 

fun hd [] = raise Empty | hd (h::t) = h;

fun nth(l,i) = 
    if i < 0 orelse i > length l
           then raise Subscript
    else
        case l of
        h::t => if i=0 then h else nth(t, i-1)|
        [] => raise Empty;

fun length [] = 0 
   | length (h::t) = 1 + length t;

fun fatt x = if x = 0 then 1 else x*fatt(x-1);

fun null x = if x = [] then true else false;

local 
fun rev(nil,l)=l
   |rev(p::r,l)=rev(r,p::l)
in
   fun reverse(list)=rev(list,[])
end;