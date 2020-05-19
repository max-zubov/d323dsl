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

    steps {
        downstreamParameterized {
            trigger('$Next_job') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
        }
    }
}



for (i in (1..4)) {
    job("MNTLAB-mzubov-child${i}-build-job") {

        parameters {
            stringParam('BRANCH_NAME')
        }

        scm {
            git {
                remote {
                    url(giturl)
                }
                branch('$BRANCH_NAME')
            }
        }
        
        
        steps {
            shell('''bash script.sh > output.txt
                     tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy output.txt''')
        }
        publishers {
            archiveArtifacts {
                pattern('${BRANCH_NAME}_dsl_script.tar.gz')
                allowEmpty(false)
                onlyIfSuccessful(false)
                fingerprint(false)
                defaultExcludes(true)
            }
        }
    }
}
