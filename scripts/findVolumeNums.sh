#!/bin/bash
ls -1 ../docs/mvz/ | awk -Fv '{print $NF}' | awk -F- '{print $1}' | sort -n
