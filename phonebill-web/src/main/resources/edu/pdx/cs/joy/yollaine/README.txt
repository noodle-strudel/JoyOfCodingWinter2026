Yollaine Brooks
CS410P: Joy of Coding
March 2026
Project 5

This program interacts with a web application that stores phone bills.
More phone calls can be added to a customer, and they can also be listed.
Phone calls can also be searched by their start time that begins between
two dates of time.

For all invocations, the -host and -port are required. To add a phone
call, type the args with an optional -print option. To search for
a phone call, use the -search flag and type the customer name. Optionally,
a range of time can be specified after the customer name (in the same order
as is shown below.)

usage: java -jar target/phonebill-1.0.0.jar [options] <args>
    args are (in this order):
        customer        Person whose name is on the phone bill
        callerNumber    Phone number of caller
        calleeNumber    Phone number of person who was called
        begin           Date and time call began (12-hour time)
        end             Date and time call ended (12-hour time)

    options are (options may appear in any order):
        -host           hostname Host computer on which the server runs
        -port           port Port on which the server is listening
        -search         Search for phone calls
        -print          Prints a description of the new phone call
        -README         Prints a README for this project and exits

    All date-times should be in the format: mm/dd/yyyy hh:mm (AM/PM)
