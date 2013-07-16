#This python module converts a specified folder of videos to .mp4 from whatever format using ffmpeg, It will also eventually parse an HTML file and replace an <object> tag with an 

import subprocess
import glob
import os
fileNames = []
fileLocation = {}
filetype = {}
def convertString(location):
    s = list(location)
    for i in range(len(s)):
        if s[i] in '\\':
            s[i] = '/'
            
    if s[len(s)-1] != '/':
        s.append('/')
    location = "".join(s)
    return location

def convertStringBack(stringTo):
    s = list(stringTo)
    for i in range(len(s)):
        if s[i] in '/':
            s[i] = '\\'
    stringTo = "".join(s)
    return stringTo

def fileTypeTester():
    FieldType = '*' + input('What\'s the file type we are converting from?')
    typeSplit = list(FieldType)
    if typeSplit[1] != '.':
        typeSplit.insert(1,'.')
    FieldType = "".join(typeSplit)
    if FieldType not in ['*.flv','*.kdb']:
        print('Not a valid file type')
    else:
        return FieldType
    return None

def massConvert(listOfFiles):
    print('Starting Conversion')
    for x in listOfFiles:
        #x = convertStringBack(x)
        print('Converting ' + x + ' to .mp4')
        convertVideotoNewFormat('.mp4', x)
    print('Finished File Conversion')
    

def convertVideotoNewFormat(newFormat, fileLoc):
    newFilePath, ext  = os.path.splitext(fileLoc)
    ffmpegString = ["ffmpeg","-i", fileLoc,newFilePath+newFormat]
    subprocess.Popen(ffmpegString)
    print("Finished convertering " + newFilePath + newFormat)
    
def findHTMLFile(myFileLocation):
    return glob.glob(myFileLocation + '*.html')
    
#This will replace old HTML flv object tag with new video tag, but it is yet to be implemented
def replaceHTML(myFile):
    pass;
    

print('''Hello, This program is a video converter that uses ffmpeg to convert. 
This Program will convert any video type requested to .mp4 (must be a video type). 
It requires you have ffmpeg installed and in your PATH under ffmpeg.
It will also replace the old lines of HTML to new HTML5(yet to be implemented).\n''')

fileLocation = input('What is the path of the files you\'d like to convert?')
fileLocation = convertString(fileLocation)
fileType = fileTypeTester()
fileNames = glob.glob(fileLocation + fileType)
massConvert(fileNames)

