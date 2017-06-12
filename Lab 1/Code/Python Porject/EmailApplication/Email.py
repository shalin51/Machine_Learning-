
# Library import for all email methods
import smtplib

print ("This is my first python Project")
print ("-"*40)
print("Email Application using Python")
print("_"*40)
print("_"*40)

# email module import
from email.mime.text import MIMEText

# Open a plain text file for reading.  For this example, assume that
# the text file contains only ASCII characters.
fileToRead = open("Documents//message.txt", 'rb')
# Creating a message form file
messageToBeSend = MIMEText(fileToRead.read())
fileToRead.close()


messageToBeSend['Subject'] = 'The contents of %s'
messageToBeSend['From'] = "shalin.ebay1@gmail.com"
messageToBeSend['To'] = "shalin.ebay2@gmail.com"

server = smtplib.SMTP('localhost')
server.sendmail(messageToBeSend['From'], messageToBeSend['To'], messageToBeSend)
server.quit()

print("Email Sent Sucessfully");
