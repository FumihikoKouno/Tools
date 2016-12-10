#!/usr/bin/env python

import argparse
import myshell

arg_parser = argparse.ArgumentParser()
arg_parser.add_argument('-p', '--prefix', 
                        help='Specify prefix character. default: "$"')
arg_parser.add_argument('-t', '--time',
                        action='store_true',
                        help='Print command input time.')
arg_parser.add_argument('-e', '--elapsed',
                        action='store_true',
                        help='Print command execute time.')
arg_parser.add_argument('log_file',
                        help='Logging file name')
args = arg_parser.parse_args()

shell = myshell.LoggingShell(args.log_file,
                             prefix=args.prefix,
                             time=args.time,
                             elapsed=args.elapsed)

shell.run()
