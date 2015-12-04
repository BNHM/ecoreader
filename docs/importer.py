#!/usr/bin/python

import urllib2
import sys
import json as j
from subprocess import check_call
import time
import os
import shlex

CONTENTS_URL = "https://api.github.com/repos/BNHM/ecoreader/contents/docs/mvz/mods"

def main():
    request = urllib2.Request(CONTENTS_URL)
    handler = urllib2.urlopen(request)

    # make sure the request was successful
    if not handler.getcode() == 200:
        print "Problem retrieving the content from github"
        # exit if the request failed
        sys.exit()

    json = j.loads(handler.readlines()[0])
    modFiles = []

    # get the url for each file and add to our modFiles list
    for file in json:
        modFiles.append(file['download_url'])

    # if there is 1+ files, then call the java class to import
    if modFiles:
        # check_call(["cd", "../"])

        # I don't think this is necessary, so I'm commenting it out
        # bTime = time.ctime()
        # check_call(["ant"])
        # if not (time.ctime(os.path.getmtime("out/production/ecoreader")) > bTime):
        #     print "Ant build failed. Didn't update mods files"
        #     sys.exit()

        # call sqlImporter w/mods_files to be updated
        args = ["java", "-cp", "lib/*:out/production/ecoreader", "renderer.sqlImporter"]
        args.extend(shlex.split(' '.join(modFiles)))
        check_call(args)

if __name__ == '__main__':
    main()