
public class StatementFormatter {

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();

        sb.append("----- Account Statement -----\n");
        sb.append("Account Number: ")
                .append(account.getAccountNumber()).append("\n");
        sb.append("Owner Name: ")
                .append(account.getOwnerName()).append("\n");
        sb.append("Balance: Rs. ")
                .append(account.getBalance()).append("\n");
        sb.append("Active: ")
                .append(account.isActive()).append("\n");

        return sb.toString();
    }
}
