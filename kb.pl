:- dynamic book/3.
:- dynamic user/2.
:- dynamic order/2.
:- use_module(library(jpl)).

table(user(Name,Id)).
table(book(Title,Author,Isbn)).
table(order(Id,Isbn)).

start :- clean_all,charge,
        interface2,
        save_file.

execute_m(X):- (X=a,register_book); %staff
	     (X=b,register_user); %staff
	     (X=c,borrow_book);
         (X=d,return);
	     (X=e,book_search);
	     (X=f,user_search); %staff
	     (X=g,order_search);
   	     (X=h,remove_user); %staff
	     (X=i,remove_book); %staff
		 (X=j,edit_user); %staff
		 (X=k,edit_book). %staff

get_title(T):- write('Enter the title of the book:'),nl,read(T).
get_author(A):- write('Enter the name of author of the book:'),nl,read(A).
get_name(N):- write('Enter the name:'),nl,read(N).
get_id(I):- write('Enter an ID:'),nl,read(I).

%Option A
empty([]).
register_book(T, A, I):-clean_all,charge,search_book(_,_,I,L1), empty(L1), insert(book,[T,A,I],L),write(L),nl,write('Book has been added to database.\n'),save_file;!,fail.

%Option B
register_user(N, I):-clean_all,charge,search_user(_,I,L1),empty(L1),insert(user,[N,I],L),write(L),nl,write('User has been added to database.'),save_file;!,fail.

%Option C
borrow_book(U, B):-clean_all,charge,search_user(_,U,L1),\+empty(L1),search_book(_,_,B,L2), \+empty(L2),find_emp(_,B,L3),empty(L3),insert(order,[U,B],L),write(L),nl,write('order successfull'),save_file;!,fail.

%Option D
return(B):-clean_all,charge,retract(order(_,B)), write('Book returned.'),save_file.

%Option E
%book_search:- write('Option a - Search by title; '), nl,
%	       write('Option b - Search by author; ' ),nl,
% 	       write('Option c - Search by id; ' ),nl,
%	       read(Option),execute_bl(Option).
%execute_bl(X):- (X=a,search_title);
%	        (X=b,search_author);
%		(X=c,search_isbn).

search_title(T,L1):-clean_all,charge,search_book(T,_,_,L1),write('List of books found: '),nl,write(L1),nl,nl,save_file.
search_author(A, L1):-clean_all,charge,search_book(_,A,_,L1),write('List of books found: '),nl,write(L1),nl,nl,save_file.
search_isbn(I,L1):-clean_all,charge,search_book(_,_,I,L1),write('List of books found: '),nl,write(L1),nl,save_file.

%Option F
%user_search:- write('Option a - Search by name; '), nl,
%	       write('Option b - Search by id; ' ),nl,
%	       read(Option),execute_bu(Option).
%execute_bu(X):- (X=a,search_name);
%	        (X=b,search_id).

search_name(N, L1):-clean_all,charge,search_user(N,_,L1),write('List of users found: '),nl,write(L1),nl,nl,save_file.
search_id(I, L1):-clean_all,charge,search_user(_,I,L1),write(I),nl,write('List of users found: '),nl,write(L1),nl,nl,save_file.

%Option G
%order_search:- write('Option a - Search by user id; '), nl,
%	       write('Option b - Search by book id; ' ),nl,
%	       read(Option),execute_be(Option).
%execute_be(X):- (X=a,search_emp_id);
%	        (X=b,search_emp_isbn).
search_emp_id(I, L):-clean_all,charge,find_emp(I,_,L),write('List of borrowed books: '),nl,write(L),save_file.
search_emp_isbn(I, L):- clean_all,charge,find_emp(_,I,L),write('List of borrowed books: '),nl,write(L),save_file.

%Option H
remove_user(I):-clean_all,charge,find_emp(I,_,L1),empty(L1),retract(user(_,I)), write('User removed'),save_file;!,fail.

%Option I
remove_book(I):-clean_all,charge,find_emp(_,I,L1),empty(L1),retract(book(_,_,I)), write('Book deleted'),save_file;!,fail.

%Option J
edit_book(T,A,I) :-clean_all,charge,remove_book(I),write('\n'), register_book(T,A,I),save_file.

%Option K
edit_user(N,I) :-clean_all,charge,remove_user(I),write('\n'), register_user(N,I),save_file.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

insert(Tab, Args, Term):- Term =.. [Tab|Args], assertz(Term).

search_book(T,A,C,L1):-findall(book(T,A,C),book(T,A,C),L1).
search_user(N,I,L1):-findall(user(N,I),user(N,I),L1).
find_emp(I,C,L1):-findall(order(I,C),order(I,C),L1).


charge:-
	open('kb.txt', append, S), write(S,''),close(S),
	['kb.txt'].

save_file :-
	tell('kb.txt'),
	listing(book),
	listing(order),
	listing(user),
	told.
clean_all:- search_book(_,_,_,L),clean(L),search_user(_,_,U),clean(U),find_emp(_,_,E),clean(E).
clean([]).
clean(X) :- remove_all(Z,X,L),
             retract(Z),
             clean(L).

remove_all(A,[A|T],T).
