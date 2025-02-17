#!/bin/bash

if [[ $1 = "" ]]; then
    echo "The script needs a maven binary file for installing local freetts libraries to repository."
    echo "The format is \"$0 <path to binary file>\""
    exit 1
fi

$1 install:install-file -Dfile=freetts/cmu_us_kal.jar -DgroupId=ua.pp.disik -DartifactId=cmu_us_kal -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$1 install:install-file -Dfile=freetts/cmulex.jar -DgroupId=ua.pp.disik -DartifactId=cmulex -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$1 install:install-file -Dfile=freetts/en_us.jar -DgroupId=ua.pp.disik -DartifactId=en_us -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$1 install:install-file -Dfile=freetts/freetts.jar -DgroupId=ua.pp.disik -DartifactId=freetts -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true