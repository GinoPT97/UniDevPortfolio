fn main() {
    println!("Primi 10 numeri di Fibonacci:");
    for i in 0..10 {
        println!("F({}) = {}", i, fibonacci(i));
    }
}

fn fibonacci(n: u32) -> u64 {
    match n {
        0 => 0,
        1 => 1,
        _ => fibonacci(n - 1) + fibonacci(n - 2)
    }
}