job ("MNTLAB-mzubov-main-build-job") {
}



for (i in (1..4)) {
    job("MNTLAB-mzubov-child${i}-build-job") {
    }
}

