fn main() {
    let n = 5;
    let risultato = fattoriale(n);
    println!("Il fattoriale di {} è {}", n, risultato);
}

fn fattoriale(n: u64) -> u64 {
    if n == 0 || n == 1 {
        1
    } else {
        n * fattoriale(n - 1)
    }
}