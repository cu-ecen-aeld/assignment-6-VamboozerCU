# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
#LICENSE = "Unknown"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"
# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
#SUMMARY = "Example of how to build an external Linux kernel module"
#DESCRIPTION = "${SUMMARY}"
#LICENSE = "GPL-2.0-only"
#LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-VamboozerCU.git;protocol=ssh;branch=main \
           file://scull-init.sh \
          "
# file://scull.rules

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "fc589caa42155e3e6051fd0a5591e9a73f39a372"

S = "${WORKDIR}/git/scull"

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${sysconfdir}/init.d/scull-init"
# /etc/udev 
# /etc/udev/rules.d 
# /etc/udev/rules.d/scull.rules 
               
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "scull-init"

RPROVIDES:${PN} += "kernel-module-scull"

#KERNEL_MODULE_AUTOLOAD += "scull"

FILES:${PN} += "${bindir}/scull_load \
                ${bindir}/scull_unload \
                ${sysconfdir}/init.d \
               "

#MODULE_EXTRA_DEPENDENCIES += "/lib/modules/${KERNEL_VERSION}/extra"

#do_configure () {
#	:
#}

do_compile () {
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull-init.sh ${D}${sysconfdir}/init.d/scull-init

    install -d ${D}${bindir}
    install -m 0755 ${S}/scull_load ${D}${bindir}/scull_load
	install -m 0755 ${S}/scull_unload ${D}${bindir}/scull_unload

    install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/scull
    install -m 0644 ${S}/scull.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/scull

    #install -d ${D}/etc/udev/rules.d
    #install -m 0644 ${WORKDIR}/scull.rules ${D}/etc/udev/rules.d
}

#PACKAGES = "${PN}"
#FILES_${PN} += "${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/scull/scull.ko"
