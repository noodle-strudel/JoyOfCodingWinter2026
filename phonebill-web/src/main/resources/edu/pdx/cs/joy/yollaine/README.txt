Yollaine Brooks
CS410P: Joy of Coding
March 2026
Project 5

This program holds information about a phone bill with at least
1 phone call. The program can save the phone bill to a text file
meant for storage so more phone calls can be added to it in the future,
or be saved in a formatted "pretty" file meant for reading. The
program is also compatible with H2 database files created with the
Converter class to store phone bills and update them with new phone
calls that way.

- Project 5
------------------------------------------------------------
usage: java -jar target/phonebill-1.0.0.jar [options] <args>
    args are (in this order):
        customer        Person whose phone bill we're modeling
                        or modifying
        callerNumber    Phone number of caller
        calleeNumber    Phone number of person who was called
        begin           Date and time call began (12-hour time)
        end             Date and time call ended (12-hour time)
    options are (options may appear in any order):

    Date and time should be in the format: mm/dd/yyyy hh:mm AM/PM
