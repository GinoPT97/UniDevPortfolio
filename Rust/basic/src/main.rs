fn main() {
    // Operazioni base
    let a = 10;
    let b = 3;
    
    println!("Somma: {} + {} = {}", a, b, a + b);
    println!("Sottrazione: {} - {} = {}", a, b, a - b);
    println!("Moltiplicazione: {} * {} = {}", a, b, a * b);
    println!("Divisione: {} / {} = {}", a, b, a / b);
    println!("Resto: {} % {} = {}", a, b, a % b);
    println!("Potenza: {}^{} = {}", a, b, i32::pow(a, b as u32));
}