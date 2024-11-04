datatype albero = Vuoto | NodoAlb of int * albero * albero;

fun vuoto () = Vuoto;

fun isEmpty Vuoto = true
  | isEmpty _ = false;

fun createNode (value, left, right) = NodoAlb(value, left, right);

fun getNodeValue (NodoAlb(value, _, _)) = value;

fun getLeftSubtree (NodoAlb(_, left, _)) = left;

fun getRightSubtree (NodoAlb(_, _, right)) = right;

fun containsValue (Vuoto, _) = false
  | containsValue (NodoAlb(value, left, right), x) =
      if value = x then
        true
      else
        containsValue(left, x) orelse containsValue(right, x);

fun preOrderTraversal Vuoto = []
  | preOrderTraversal (NodoAlb(value, left, right)) =
      [value] :: preOrderTraversal left @ preOrderTraversal right;

fun inOrderTraversal Vuoto = []
  | inOrderTraversal (NodoAlb(value, left, right)) =
      inOrderTraversal left @ [value] :: inOrderTraversal right;

fun postOrderTraversal Vuoto = []
  | postOrderTraversal (NodoAlb(value, left, right)) =
      postOrderTraversal left @ postOrderTraversal right @ [value];

val albero = NodoAlb (3, NodoAlb (2, NodoAlb (1, Vuoto, Vuoto), NodoAlb (4, Vuoto, Vuoto)), NodoAlb (6, NodoAlb (5, Vuoto, Vuoto), NodoAlb (7, Vuoto, Vuoto)));

val isEmptyTree = isEmpty albero; (* false *)

val nodeValue = getNodeValue albero; (* 3 *)

val leftSubtree = getLeftSubtree albero; (* NodoAlb (2, NodoAlb (1, Vuoto, Vuoto), NodoAlb (4, Vuoto, Vuoto)) *)

val rightSubtree = getRightSubtree albero; (* NodoAlb (6, NodoAlb (5, Vuoto, Vuoto), NodoAlb (7, Vuoto, Vuoto)) *)

val containsValue1 = containsValue (albero, 2); (* true *)
val containsValue2 = containsValue (albero, 8); (* false *)

val preOrder = preOrderTraversal albero; (* [[3], [2, 1], [4], [6, 5], [7]] *)
val inOrder = inOrderTraversal albero; (* [[1, 2], [3], [4], [5, 6], [7]] *)
val postOrder = postOrderTraversal albero; (* [[1, 2], [4], [5, 6], [7], [3]] *)
