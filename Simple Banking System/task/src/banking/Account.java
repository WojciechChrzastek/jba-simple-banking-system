package banking;

public class Account {
  long cardNumber;
  String pin;
  int balance;

  public Account(Long cardNumber, String pin, int balance) {
    this.cardNumber = cardNumber;
    this.pin = pin;
    this.balance = balance;
  }

  public long getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(long cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Account account = (Account) o;

    if (cardNumber != account.cardNumber) return false;
    if (balance != account.balance) return false;
    return pin.equals(account.pin);
  }

  @Override
  public int hashCode() {
    int result = (int) (cardNumber ^ (cardNumber >>> 32));
    result = 31 * result + pin.hashCode();
    result = 31 * result + balance;
    return result;
  }

  @Override
  public String toString() {
    return "Account{" +
            "cardNumber=" + cardNumber +
            ", pin='" + pin + '\'' +
            ", balance=" + balance +
            '}';
  }
}

