package banking;

public class Account {
  String cardNumber;
  String pin;
  int balance;

  public Account(String cardNumber, String pin, int balance) {
    this.cardNumber = cardNumber;
    this.pin = pin;
    this.balance = balance;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public String getPin() {
    return pin;
  }

  public int getBalance() {
    return balance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Account account = (Account) o;

    if (balance != account.balance) return false;
    if (!cardNumber.equals(account.cardNumber)) return false;
    return pin.equals(account.pin);
  }

  @Override
  public int hashCode() {
    int result = cardNumber.hashCode();
    result = 31 * result + pin.hashCode();
    result = 31 * result + balance;
    return result;
  }

  @Override
  public String toString() {
    return "Account{" +
            "cardNumber='" + cardNumber + '\'' +
            ", pin='" + pin + '\'' +
            ", balance=" + balance +
            '}';
  }
}

