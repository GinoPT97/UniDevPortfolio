fn main() {
    let n = 29;
    if is_primo(n) {
        println!("{} è un numero primo", n);
    } else {
        println!("{} non è un numero primo", n);
    }
    
    // Primi numeri primi
    println!("\nPrimi 20 numeri primi:");
    let mut count = 0;
    let mut num = 2;
    while count < 20 {
        if is_primo(num) {
            print!("{} ", num);
            count += 1;
        }
        num += 1;
    }
    println!();
}

fn is_primo(n: u64) -> bool {
    if n < 2 {
        return false;
    }
    for i in 2..=(n as f64).sqrt() as u64 {
        if n % i == 0 {
            return false;
        }
    }
    true
}