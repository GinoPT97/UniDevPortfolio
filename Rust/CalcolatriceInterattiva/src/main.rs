use std::io;

fn main() {
    println!("=== Calcolatrice Rust ===");
    
    let mut input = String::new();
    
    println!("Inserisci il primo numero:");
    io::stdin().read_line(&mut input).expect("Errore lettura");
    let a: f64 = input.trim().parse().expect("Inserisci un numero valido");
    
    input.clear();
    println!("Inserisci il secondo numero:");
    io::stdin().read_line(&mut input).expect("Errore lettura");
    let b: f64 = input.trim().parse().expect("Inserisci un numero valido");
    
    println!("\nRisultati:");
    println!("{} + {} = {}", a, b, a + b);
    println!("{} - {} = {}", a, b, a - b);
    println!("{} * {} = {}", a, b, a * b);
    println!("{} / {} = {}", a, b, a / b);
}