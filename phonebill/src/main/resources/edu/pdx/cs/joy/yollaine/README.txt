Yollaine Brooks
CS410P: Joy of Coding
January 2026
Project 1

This program holds information about a phone bill with at least 1 phone call.

usage: java -jar target/phonebill-1.0.0.jar [options] <args>
    args are (in this order):
        customer        Person whose phone bill we’re modeling
        callerNumber    Phone number of caller
        calleeNumber    Phone number of person who was called
        begin           Date and time call began (24-hour time)
        end             Date and time call ended (24-hour time)
    options are (options may appear in any order):
        -print          Prints a description of the new phone call
        -README         Prints a README for this project and exits
    Date and time should be in the format: mm/dd/yyyy hh:mm