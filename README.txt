google forum post: http://code.google.com/p/google-web-toolkit/issues/detail?id=134 regarding 64-bit linux in hosted mode:

	The GWT hosted web browser ships with a 32-bit version of the SWT bindings,
	so the browser does not work in 64-bit Linux.

	Workarounds:
	Some tips for getting the 32-bit version to work:

	Use a 32-bit JDK with 32 bit binary compatability enabled.

	Set the environment variable LD_LIBRARY_PATH to the mozilla-1.7.12
	directory in your GWT distribuiton before starting the GWT shell.

