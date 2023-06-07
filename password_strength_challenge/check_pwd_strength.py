import string
import os

LOW = "low"
MEDIUM = "medium"
HIGH = "high"


def contains_required_chars(pwd):
    contains_special = False
    contains_lower = False
    contains_upper = False
    contains_number = False

    for chr in pwd:
        if chr in set(string.punctuation):
            contains_special = True
        if chr in str(range(10)):
            contains_number = True
        if chr in set(string.ascii_lowercase):
            contains_lower = True
        if chr in set(string.ascii_uppercase):
            contains_upper = True
    
    return contains_special and contains_lower and contains_number and contains_upper 


def get_strength(pwd, pwd_set):
    if not contains_required_chars(pwd) or pwd in pwd_set or len(pwd)<8: 
        return LOW

    if len(pwd)<15: return MEDIUM
    return HIGH


if __name__ == '__main__':
    print("Loading password dictionary")

    script_path = os.path.realpath(os.path.dirname(__file__))
    pwd_dict_path = os.path.join(script_path, "pwd_dict.txt")

    with open(pwd_dict_path, "r") as f: 
        pwd_set = set(line.strip() for line in f)

    print("Loaded file")

    while True:
        try:
            pwd = input("Enter password: ")
            print(get_strength(pwd, pwd_set))
        except KeyboardInterrupt: break