# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# SRC_URI = "git://git@github.com/cu-ecen-aeld/<your assignments repo>;protocol=ssh;branch=master"
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-VamboozerCU.git;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "34314586047099f38df6fad9e0cab6ee4e87ca01"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git"

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/aesd-char-driver"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/aesdsocket"
FILES:${PN} += "${sysconfdir}/init.d/aesdsocket-start-stop"
#FILES:${PN} += "${sysconfdir}/rsyslog.conf"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
#TARGET_LDFLAGS += "-pthread -lrt"
TARGET_CC_ARCH += "${LDFLAGS}"
RDEPENDS_${PN} = "libgcc"

inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "aesdsocket-start-stop"

#RPROVIDES:${PN} += "kernel-module-aesdchar"
RPROVIDES:${PN} += "kernel-module-aesd-assignments"
KERNEL_MODULE_AUTOLOAD += "aesdchar"

FILES:${PN} += "${bindir}/aesdchar_load \
                ${bindir}/aesdchar_unload \
                ${sysconfdir}/init.d \
               "

do_configure () {
	:
}

do_compile () {
	# oe_runmake CC="${CC}" CFLAGS="${CFLAGS}" INCLUDES="${INCLUDES}" LDFLAGS="${LDFLAGS}"
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
	install -d ${D}${bindir}
	install -m 0755 ${S}/server/aesdsocket ${D}${bindir}/
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/server/aesdsocket-start-stop.sh ${D}${sysconfdir}/init.d/aesdsocket-start-stop

    install -m 0755 ${S}/aesd-char-driver/aesdchar_load ${D}${bindir}/aesdchar_load
	install -m 0755 ${S}/aesd-char-driver/aesdchar_unload ${D}${bindir}/aesdchar_unload

	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/aesdchar
    install -m 0644 ${S}/aesd-char-driver/aesdchar.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/aesdchar
}