# The Gringotts Bank Integration

## Introduction

Carta is on a mission to create owners and in this pursuit it turns out that _witches and wizards_ also own shares in companies. They can _manage_ their shares using Carta, but they are not able to buy/sell shares because they uniquely use **Gringotts Bank**. Carta has not yet built an integration with Gringotts.

Carta does already integrate with two other banks: SweetBank and AcmeFinancials. Part of this integration involves parsing transaction data and generating reports. The current codebase can already parse files and print reports for those two; we need to **add support for the Gringotts transaction data**.

## The Problem

Update the provided code to recognize a new input file format and **print out a small report** identifying the account whose **average withdrawal amount** was largest.

Refactor the existing codebase as appropriate to support this new integration.

You can print the current reports as follows:
```
gradle run --args="acme_bank"
gradle run --args="sweet_bank"
```

To install Gradle 

### Mac
    brew install gradle

###   Windows
    choco install gradle

### Install manually
    https://gradle.org/install/

### File Format

After much trouble, the goblins at Gringotts handed us the specification for the file format:

Each file contains a collection of **Transaction Batches**, each of which is associated with a collection of **Transactions**. The file format looks roughly like this:
```
[Transaction Batch ID]
[Transaction]
[Transaction]
[Transaction...]
[Transaction Batch ID]
[Transaction...]
[Transaction Batch ID...]
...
```

Here's an example:
```
GTTB0001213929322
c74e460d-e3b3-4f5b-b84e-b3347dde04dd
128172817HP991819309
129991299HG009183881
USD100000
d41e8c45-aeae-4882-b22a-549ceb756a21
011111111LM000827771
011111121BL000018822
USD2938982349
ea699f2e-9ed1-44d8-af15-46b9418bc0d3
129991299HG009183881
128172817HP991819309
USD100
```

#### Transaction Batch ID
```
GTTB0001213929322
├──┘├───────────┘
│   └ Batch ID (13 numbers)
└ Bank Identifier (4 letters, always "GTTB")
```

#### Transaction

Each Transaction is a block of 4 lines in the input data:
```
[Transaction ID]
[Source Account]
[Destination Account]
[Transfer Amount]
```

##### Transaction ID
```
c74e460d-e3b3-4f5b-b84e-b3347dde04dd
├──────────────────────────────────┘
└ UUID v4 (8-4-4-4-12 hex digits)
```

#### Source Account
```
128172817HP991819309
├───────┘├┘├───────┘
│        │ └ Account ID: (9 numbers)
│        └ Account Holder (2 letters)
└ Bank ID (9 numbers)
```

##### Destination Account

Same as [Source Account](#source-account).

##### Transfer Amount

The amount of money _withdrawn_ from the Source Account and _deposited_ to the Desination Account.
```
USD100000
├─┘├────┘
│  └ Transferred Amount (in cents)
└ Currency Code (3 letters, always `USD`) (ISO 4217 standard)
```

## Evaluation Criteria

Your submission will be evaluated along these axes:
- Is its output **correct**?
- Is the code **clear** and **readable**? (the current version of the code is far from it)
- Is the code of **production quality**?
- Does the code handle large data sets?
- Does the code demonstrate good `git` commit hygiene?

## Submitting Your Work

Submit your code as a zip file (no GitHub links, please!) using the submission link provided by your recruiter. Name your file `YOURINITIALS-CARTA.zip`, e.g. `AB-CARTA.zip` or `AB-CARTA.tar.gz`

Your submission should also include a `SUBMISSION.txt` describing any key design decisions and documenting any assumptions you made.

**Your submission should also include a file called `solution.txt` with the output of applying your solution to the sample Gringotts data in the `assets` directory.**

So we can ensure that your assignment remains anonymous during grading, please do not include your name anywhere in your code or in your `SUBMISSION.txt`.

Ensure all your changes are committed, and then run these two commands from the directory root to zip your solution with an anonymized git history:
```
git filter-branch --env-filter '
  export GIT_AUTHOR_EMAIL="anonymous"
  export GIT_AUTHOR_NAME="anonymous"
  export GIT_COMMITTER_EMAIL="anonymous"
  export GIT_COMMITTER_NAME="anonymous"
  ' -- --all

tar -czvf AB-CARTA.tar.gz \
  SUBMISSION.txt \
  solution.txt
```

