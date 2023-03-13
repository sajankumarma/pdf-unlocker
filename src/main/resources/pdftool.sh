#!/bin/bash
if [[ $# -eq 0 ]] || [[ $1 == "-h" ]] || [[ $1 == "--help" ]]; then
  echo "USAGE: <filename> <password if unlock required> <page number range e.g. 1-5 if spilt required> <targetfile>"
   echo "e.g     pdftool bill-locked.pdf password 1-2 bill.pdf"
  exit 1
fi

filename=$1
password=""
startingPageNumber=-1
endingPageNumber=-1
targetfile=""

if [[ $2 == *-* ]]; then
  pages=(${2//-/ })
  startingPageNumber=${pages[0]}
  endingPageNumber=${pages[1]}
else
  password=$2
fi

if [ ! -z "$3" ]; then
  if [[ $3 == *-* ]]; then
    pages=(${3//-/ })
    startingPageNumber=${pages[0]}
    endingPageNumber=${pages[1]}
  else
    targetfile=$3
  fi
fi

if [ ! -z "$4" ]; then
  targetfile=$4
fi

if [ ! -z "$filename" ]; then
  java -jar /home/rsin-schoudhary-l14/work/tools/pdftool/pdf-unlocker.jar "$filename" "$password" "$startingPageNumber-$endingPageNumber" "$targetfile"
else
  echo "Error: filename is not provided"
fi
