mvn test -Dtest=LoginTest > login_out.txt 2>&1
type login_out.txt
mvn test -Dtest=TransferFundsTest > transfer_out.txt 2>&1
type transfer_out.txt
