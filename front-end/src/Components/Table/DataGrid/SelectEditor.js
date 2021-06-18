import Select from 'react-select';

export function SelectEditor({ value, onChange, options, rowHeight, menuPortalTarget }) {
  return (
    <Select
      autoFocus
      defaultMenuIsOpen
      value={options.find(o => o.value === value)}
      onChange={o => onChange(o.value)}
      options={options}
      menuPortalTarget={menuPortalTarget}
      styles={{
        control: (provided) => ({
          ...provided,
          height: rowHeight - 1,
          minHeight: 30,
          lineHeight: 'normal'
        }),
        dropdownIndicator: (provided) => ({
          ...provided,
          height: rowHeight - 1
        })
      }}
    />
  );
}