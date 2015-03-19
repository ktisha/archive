#ifndef _BOOLVECTORT__H
#define _BOOLVECTORT__H

#include <vector>
#include <stdexcept>
#include <cstdlib>
#include <iostream>

template <typename T = int32_t>
class BoolVectorT{
    private:
        typedef std::vector<bool> Values;
        size_t mySize;
        T myInt;
        static size_t const maxSize = sizeof(T) * 8;

    public:
        BoolVectorT ()
            : mySize(0)
            , myInt(0)
        {}

        BoolVectorT (Values const &in);
        
        void push_back(const bool &value);
        void set (size_t index, bool val);

        bool operator[] (size_t index) const;

        BoolVectorT<T>& operator++();
        BoolVectorT<T>  operator++(int i);
        BoolVectorT<T>& operator--();
        BoolVectorT<T>  operator--(int i);
        
        size_t size()       const {return mySize; }
        size_t capacity()   const {return maxSize;}

        void resize(size_t size, bool value = false);
    
    private:
        void cleanTail();
};

typedef BoolVectorT<> BoolVector;

template <typename T>
    BoolVectorT<T>::BoolVectorT (Values const &in)
        : myInt(0)
{

    if (in.size() > maxSize) 
        throw std::logic_error("Error creating BoolVector: input vector size > maxSize!");

    for (int i=0; i < in.size(); ++i)
    {
        myInt |= ((T)(in[i])) << i;
    }
    mySize = in.size();
    cleanTail();
}

template <typename T>
    void BoolVectorT<T>::set (size_t index, bool val)//index is byte number(starts from 0)
{
    if (index >= maxSize) 
        throw std::logic_error("Error BoolVector::set(index, val): index >= BoolVector max size.");
    
    if (index >= mySize) 
        throw std::out_of_range("Error BoolVector::set(index, val): index >= BoolVector size.");
      
    switch (val) 
    {
        case false:
            myInt &= ~(((T)1)<<index);
            break;
        case true:
            myInt |= (((T)1)<<index);
            break;
    }
}

template <typename T>
    bool BoolVectorT<T>::operator[] (size_t index) const
{
    if (index >= mySize) 
        throw std::logic_error("Error BoolVector::operator[index]: index >= BoolVector size !");
    return (myInt & (((T)1)<<index) );
}

template <typename T>
    BoolVectorT<T>& BoolVectorT<T>::operator++()
{
    if (myInt == ~((T)0)) 
        throw std::logic_error("Error BoolVector::operator++: result size > maxSize !");   

    ++myInt;
    cleanTail();
    return *this;    
}

template <typename T>
    BoolVectorT<T> BoolVectorT<T>::operator++(int i)
{    
    BoolVectorT<T> tempBV(*this);
    ++(*this);
    return tempBV;    
}

template <typename T>
    BoolVectorT<T>& BoolVectorT<T>::operator--()
{
    if (myInt == 0) 
        throw std::logic_error("Error BoolVector::operator++: result size < 0 bit!");   
    --myInt;
    cleanTail();
    return *this;    
}

template <typename T>
    BoolVectorT<T> BoolVectorT<T>::operator--(int i)
{
    BoolVectorT<T> tempBV(*this);
    --(*this);
    return tempBV;    
}

template <typename T>
    void BoolVectorT<T>::resize(size_t size, bool value )
{
    if (size > maxSize) 
        throw std::logic_error("Error BoolVector::resize(): new size > maxSize !");
                                           
    if (mySize < size)
    {
        if (value)
            myInt |=    (~((T)0)) << mySize ;
        else
            myInt &=  ~((~((T)0)) << mySize) ;
    }

    mySize = size;
    cleanTail();
}

template <typename T>
    void BoolVectorT<T>::cleanTail()
{
    if (mySize < maxSize)
        myInt &= ~((~((T)0)) << mySize);
}

template <typename T>
    void BoolVectorT<T>::push_back(const bool &value)
{
    if (mySize < maxSize) {
        if (value) {
            myInt |= ((T)1) << mySize;
        }
        mySize++;
    } else 
        throw std::logic_error("Error BoolVector::push_back():vector is full!");
    
}

template<typename T>
    std::ostream& operator<<(std::ostream &stream, const BoolVectorT<T> &b)
{
    for (int i=0; i < b.size(); ++i)
        stream << b[i];
    return stream;
}

std::ostream& operator<< (std::ostream &stream, const BoolVector &b);

#endif

