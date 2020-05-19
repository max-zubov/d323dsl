def giturl = 'https://github.com/max-zubov/d323dsl'
job ("MNTLAB-mzubov-main-build-job") {

    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Choose branch')
            choiceType('SINGLE_SELECT')
            groovyScript {
              script("""("git ls-remote -h ${giturl}").execute().text.readLines().collect {
                      it.split()[1].replaceAll(\'refs/heads/\', \'\')}.sort()""")
            }
        }
        activeChoiceParam('Next_job') {
            description('Choose job')
            choiceType('CHECKBOX')
            groovyScript {
                script('''return ['MNTLAB-mzubov-child1-build-job',
                            'MNTLAB-mzubov-child2-build-job',
                            'MNTLAB-mzubov-child3-build-job',
                            'MNTLAB-mzubov-child4-build-job']''')
            }
        }
    }

}



for (i in (1..4)) {
    job("MNTLAB-mzubov-child${i}-build-job") {
    }
}
