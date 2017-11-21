FROM docker.adeo.no:5000/bekkci/maven-builder
ADD / /source
RUN build


# TODO oppsett for nais

FROM docker.adeo.no:5000/bekkci/skya-deployer as deployer
FROM docker.adeo.no:5000/bekkci/backend-smoketest as smoketest

