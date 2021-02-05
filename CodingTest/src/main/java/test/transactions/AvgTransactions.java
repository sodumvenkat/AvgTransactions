package test.transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AvgTransactions {

    public static void main(String[] args) throws ParseException {
        AvgTransactions avgTransactions = new AvgTransactions();
        List<String> transactionsList = new ArrayList<>();
        transactionsList.add("T1234, 2020-03-01 , 3:15 pm, start");
        transactionsList.add("T1235, 2020-03-01 , 3:16 pm, start");
        transactionsList.add("T1236, 2020-03-01 , 3:17 pm, start");
        transactionsList.add("T1234, 2020-03-01 , 3:18 pm, End");
        transactionsList.add("T1235, 2020-03-01 , 3:18 pm, End");

        List<Transaction> lo = avgTransactions.convertToObject(transactionsList);
        Set<String> uniqueTrans = lo.stream().map(Transaction::getTransactionId).collect(Collectors.toSet());
        uniqueTrans.iterator().forEachRemaining(uniqueTran -> {
            Date start = avgTransactions.getTransactionDateAndTime(uniqueTran, lo, "start");
            Date end = avgTransactions.getTransactionDateAndTime(uniqueTran, lo, "End");
            if(Objects.nonNull(end)) {
                long diffInMillies = Math.abs(end.getTime() - start.getTime());
                System.out.println("For TransactionId: "+uniqueTran+" Difference in seconds: "+diffInMillies/1000);
            } else {
                System.out.println("TransactionId: "+uniqueTran+" didn't ended.");
            }
        });
    }

    private Date getTransactionDateAndTime(String tranId, List<Transaction> lt, String status) {
        Optional<Date> date = lt.stream()
                .filter(transaction -> transaction.getTransactionId().equals(tranId) && transaction.getStatus().equals(status))
                .map(Transaction::getTransactionTime)
                .findFirst();
        return date.isPresent() ? date.get() : null;
    }

    private List<Transaction> convertToObject(List<String> l) throws ParseException {
        List<Transaction> transactions = new ArrayList<Transaction>();
        for(String tran : l) {
            String[] ss = tran.split(",");
            Transaction transaction = new Transaction();
            transaction.setTransactionId(ss[0]);
            transaction.setStatus(ss[3].trim());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm a");
            transaction.setTransactionTime(formatter.parse(ss[1].trim() + ss[2]));

            transactions.add(transaction);
        }

        return transactions;
    }

    public class Transaction {
        private String transactionId;
        private String status;
        private Date transactionTime;

        public Date getTransactionTime() {
            return transactionTime;
        }

        public void setTransactionTime(Date transactionTime) {
            this.transactionTime = transactionTime;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
