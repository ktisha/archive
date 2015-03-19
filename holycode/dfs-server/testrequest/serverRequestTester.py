#! /usr/bin/python

import sys, dircache, os

def compare(tested_file_name, pattern_file_name):
	tst_str = open(tested_file_name, "r").read()
	ptt_str = open(pattern_file_name, "r").read()
	return (tst_str == ptt_str)
	

def report(descripton, passed):
	result = "failed"
	if (passed): 
		result = "passed"
	print descripton, "...", result

def main(args=sys.argv):
	task_order = open("testorder", "r")
	log_file = open("log", "w")
	list = task_order.readlines()
	task_order.close()

	pattern_folder = "goldresponse/"
	param = "java -jar ../pseudoClient/dist/pseudoClient.jar"
	clue = "-r"
	upload = "-u ./test.upload"
	download = "-d ./test.down"
	port1 = "-p 9000"
	port2 = "-p 9001"

	os.system("rm -rf ../dfs-test.db")
	os.system("cp -r ../dfs-test.db.bkp ../dfs-test.db")
	os.system("./launcher.sh 2 9000")

	total = 0
	passed_amount = 0

	for task in list:
		test = task.strip()
		test_name = task.strip()
		pattern = open(pattern_folder + test + ".xml", "r").read()
			
		command = param + ' ' + clue + ' ' + test + ".task.xml"

		if (test_name[:6] == "agent/"):
			test_name = test_name[6:]
			if (test_name[-1] == "2"):
				command += ' ' + port2
			else :
				command += ' ' + port1

		if (test_name[:8] == "download"):
			command += ' ' + download

		if (test_name[:6] == "upload"):
			command += ' ' + upload


		#print command
		(child_stdin, child_stdout, child_stderr) = os.popen3(command)
		result = child_stdout.read()
		is_passed = (result == pattern)

		if (test_name == "login"):
			is_passed = (result.find("<status>ok</status>") != -1)
		
		if (test_name == "downloadFile"):
			is_passed = compare("test.down", "test.upload")

		total += 1
		if (is_passed):
			passed_amount += 1
		else:
			task_xml = open(test + ".task.xml", "r").read()
			log_file.write("---------------"+test_name+"------------------------\n")
			log_file.write(command +"\n")
			log_file.write("---------------request------------------------\n")
			log_file.write(task_xml+"\n")
			log_file.write("---------------result------------------------\n")
			log_file.write(result+"\n")
			log_file.write("---------------gold--------------------------\n")
			log_file.write(pattern+"\n")
			log_file.write("----------------------------------------------------\n")

		report(test + " test", is_passed)

	log_file.close()
	print "---------------------------------------"
	print "succesfully passed ", passed_amount, " tests from ", total
	print "details of failed tests look at \"log\" file"
	os.system("cat ./forkill.pids | xargs kill");
	os.system("rm test.down");

	if (passed_amount != total):
		return 1
	else:
		return 0

if __name__ == '__main__':
	sys.exit(main())
