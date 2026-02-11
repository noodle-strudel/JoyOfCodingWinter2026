Yollaine Brooks
CS410P: Joy of Coding
February 2026
Project 3

This program holds information about a phone bill with at least
1 phone call. The program can save the phone bill to a text file
meant for storage or be saved in a formatted file meant for reading.

usage: java -jar target/phonebill-1.0.0.jar [options] <args>
    args are (in this order):
        customer        Person whose phone bill we're modeling
        callerNumber    Phone number of caller
        calleeNumber    Phone number of person who was called
        begin           Date and time call began (24-hour time)
        end             Date and time call ended (24-hour time)
    options are (options may appear in any order):
        -pretty file    Pretty print the phone bill to a text file
                        or standard out (-pretty -)
        -textFile file  Where to read/write the phone bill
        -print          Prints a description of the new phone call
        -README         Prints a README for this project and exits
    Date and time should be in the format: mm/dd/yyyy hh:mm